package com.voice.law.controller.rest

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.bean.copier.CopyOptions
import com.voice.law.domain.*
import com.voice.law.jpa.RoleRepository
import com.voice.law.jpa.UserRepository
import com.voice.law.jpa.UserRoleRepository
import com.voice.law.mapper.UserMapper
import com.voice.law.service.SecurityService
import com.voice.law.service.UserService
import com.voice.law.util.JwtUtil
import com.voice.law.util.MD5Util
import com.voice.law.util.SysConstant
import com.voice.law.util.WebResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest
import java.text.SimpleDateFormat

@RestController
@RequestMapping("/rest/user")
class UserController {
    @Autowired
    private UserRepository userRepository
    @Autowired
    private RoleRepository roleRepository
    @Autowired
    UserRoleRepository userRoleRepository
    @Autowired
    SecurityService securityService
    @Autowired
    StringRedisTemplate redisTemplate
    @Autowired
    UserService userService
    @Autowired
    UserMapper userMapper

    Logger logger = LoggerFactory.getLogger(this.class)

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    WebResult login(String username, String password) {
        User user = userRepository.findByUsernameAndDeleted(username, 0)
        if (user == null || !(MD5Util.compare(password, user.slat, user.password)) || user.userTypeEnum != UserTypeEnum.WEB) {
            return WebResult.generateFalseWebResult("用户名密码错误！")
        }
        user.lastLoginTime = new Date()
        userRepository.saveAndFlush(user)
        return WebResult.generateTrueWebResult(securityService.generateAuthTokenInfo(user), "登陆成功！")
    }

    /**
     * 刷新access_token
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/generateToken")
    WebResult generateToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(SysConstant.REFRESH_TOKEN)
        try {
            AuthUser authUser = JwtUtil.parseToken(refreshToken)
            if (authUser.option != SysConstant.REFRESH_TOKEN) {
                return WebResult.generateUnTokenWebResult()
            }
            User user = userRepository.findByUsernameAndDeleted(authUser.username, 0)
            String oldToken = redisTemplate.opsForValue().get(SysConstant.REDIS_KEY_USER_REFRESH_TOKEN + user.username)
            //只要到这，说明token解析成功，token存在
            //token 与 redis中存储的不一致 说明当前token已被更改，在其他设备重新登录了。
            if (oldToken != refreshToken) {
                return new WebResult(401, "检测到您在其他设备登录，当前设备已下线！", null)
            }
            return WebResult.generateTrueWebResult(securityService.generateAuthTokenInfo(user), "生成token成功！")
        } catch (Exception e) {
            logger.error("refresh_token解析失败！")
            return WebResult.generateUnTokenWebResult()
        }
    }

    /**
     * 添加或者修改用户（后台）
     * @return
     */
    @PostMapping("/addOrUpdateUser")
    @PreAuthorize("hasRole('ADMIN')")
    WebResult addOrUpdateUser(User paramUser, HttpServletRequest request) {
        String sexEnumStr = request.getParameter("sexEnumStr")
        String educationEnumStr = request.getParameter("educationEnumStr")
        String birthdayStr = request.getParameter("birthdayStr")
        String occupationDateStr = request.getParameter("occupationDateStr")
        String occupationEndDateStr = request.getParameter("occupationEndDateStr")

        //加密密码
        if (paramUser.password) {
            if (paramUser.password.length() > 16) {
                return WebResult.generateFalseWebResult("密码长度不能大于16位！")
            }
            String slat = UUID.randomUUID().toString()
            paramUser.slat = slat
            paramUser.password = MD5Util.getMD5(paramUser.password, slat)
        }

        User user
        if (paramUser.id) {
            //更新
            user = userRepository.findById(paramUser.id).orElse(null)
            if (!user) {
                return WebResult.generateFalseWebResult("用户不存在！")
            }
            if (user.userTypeEnum != UserTypeEnum.WEB) {
                return WebResult.generateFalseWebResult("仅支持修改web用户信息，该用户为非web用户！")
            }
            if (paramUser.username != null) {
                return WebResult.generateFalseWebResult("禁止修改用户名！")
            }
            user.updaterId = securityService.getCurrentUser().id
        } else {
            //新增
            if (!(paramUser.username && paramUser.password)) {
                return WebResult.generateFalseWebResult("缺少必填项！")
            }
            User existUser = userRepository.findByUsernameAndDeleted(paramUser.username, 0)
            if (existUser) {
                return WebResult.generateFalseWebResult("该用户名已存在！")
            }
            user = new User()
            user.userTypeEnum = UserTypeEnum.WEB
            user.creatorId = securityService.getCurrentUser().id
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")

        if (sexEnumStr)
            user.sexEnum = SexEnum.valueOf(sexEnumStr)
        if (educationEnumStr)
            user.educationEnum = EducationEnum.valueOf(educationEnumStr)
        if (birthdayStr)
            user.birthday = sdf.parse(birthdayStr)
        if (occupationDateStr)
            user.occupationDate = sdf.parse(occupationDateStr)
        if (occupationEndDateStr)
            user.occupationEndDate = sdf.parse(occupationEndDateStr)

        BeanUtil.copyProperties(paramUser, user, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true))
        try {
            userRepository.saveAndFlush(user)
        } catch (Exception e) {
            e.printStackTrace()
            logger.error("修改用户信息保存错误。user id: " + user.id)
            return WebResult.generateTrueWebResult("系统错误！")
        }
        return WebResult.generateTrueWebResult("保存成功！")
    }


    /**
     * 获取当前用户
     * @return
     */
    @RequestMapping("/currentUser")
    WebResult getCurrentUser(String option) {
        User user = securityService.getCurrentUser()
        WebResult.generateTrueWebResult(userService.formatPropertyToMap(user, option == "detail"))
    }

    /**
     * 通过id获取用户详情
     * @return
     */
    @GetMapping("/getUserDetailsById")
    WebResult getUserDetailsById(Integer userId) {
        if (!userId) {
            return WebResult.generateFalseWebResult("param userId not found!")
        }
        User user = userRepository.findByIdAndDeleted(userId, 0)
        if (!user) {
            return WebResult.generateFalseWebResult("用户不存在！")
        }
        def result = userService.formatPropertyToMap(user, true)
        return WebResult.generateTrueWebResult(result)
    }

    /**
     * 修改密码
     */
    @PostMapping("/changePassword")
    WebResult changePassword(String oldPassword, String newPassword) {
        if (!(oldPassword && newPassword)) {
            return WebResult.generateFalseWebResult("param not found!")
        }
        if (newPassword.length() > 16) {
            return WebResult.generateFalseWebResult("密码长度不能大于16位！")
        }
        User user = securityService.getCurrentUser()
        if (!(MD5Util.compare(oldPassword, user.slat, user.password))) {
            return WebResult.generateFalseWebResult("原密码不正确！")
        }
        String slat = UUID.randomUUID().toString()
        user.slat = slat
        user.password = MD5Util.getMD5(newPassword, slat)
        try {
            userRepository.saveAndFlush(user)
        } catch (Exception e) {
            e.printStackTrace()
            logger.error("修改密码保存错误。user id: " + user.id)
            return WebResult.generateTrueWebResult("系统错误！")
        }
        return WebResult.generateTrueWebResult("SUCCESS", "密码修改成功！")
    }

    /**
     * 用户列表
     */
    @GetMapping("/userList")
    @PreAuthorize("hasRole('ADMIN')")
    WebResult userList(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "nickName", required = false) String nickName,
            @RequestParam(name = "userTypeEnumStr", required = false) String userTypeEnumStr,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber
    ) {
        pageNumber = pageNumber ? pageNumber : 1
        pageSize = pageSize ? pageSize : 20

        if (pageNumber < 1 || pageSize <= 0) {
            return WebResult.generateFalseWebResult("参数非法！")
        }

        def queryParams = [
                username       : username ? ("%" + username + "%") : null,
                nickName       : nickName ? ("%" + nickName + "%") : null,
                userTypeEnumStr: userTypeEnumStr ? userTypeEnumStr : null,
                pageSize       : pageSize,
                offset         : pageSize * (pageNumber - 1),
                option         : "list",
        ]

        def queryResult = userMapper.userList(queryParams)
        def countResult = userMapper.userList(queryParams << [option: "count"])

        def result = [:]
        if (queryResult) {
            result.list = queryResult.collect { Map row ->
                SexEnum sexEnum = row.sexEnum ? SexEnum.valueOf(row.sexEnum as String) : null
                [
                        userId       : row.userId,
                        username     : row.username,
                        nickName     : row.nickName,
                        avatar       : row.avatar,
                        sexEnum      : sexEnum ? [
                                key : sexEnum.toString(),
                                desc: sexEnum.getDesc()
                        ] : null,
                        createTime   : row.createTime ? SysConstant.yyyyMMddHHmmssSdf.format(row.createTime as Date) : null,
                        lastLoginTime: row.lastLoginTime ? SysConstant.yyyyMMddHHmmssSdf.format(row.lastLoginTime as Date) : null
                ]
            }
            result.pageSize = queryResult.size()
            result.totalCount = countResult[0].totalCount
        } else {
            result.list = []
            result.pageSize = 0
            result.totalCount = 0
        }

        return WebResult.generateTrueWebResult(result)
    }
}