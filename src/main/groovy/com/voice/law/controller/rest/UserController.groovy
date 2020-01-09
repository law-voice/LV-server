package com.voice.law.controller.rest

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.bean.copier.CopyOptions
import com.voice.law.domain.*
import com.voice.law.jpa.RoleRepository
import com.voice.law.jpa.UserRepository
import com.voice.law.jpa.UserRoleRepository
import com.voice.law.service.SecurityService
import com.voice.law.service.UserService
import com.voice.law.util.JwtUtil
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
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
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
        if (user == null || !(user.password == password)) {
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
    WebResult generateToken(HttpServletRequest request, HttpServletResponse response) {
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
     * 添加用户（后台）
     * @return
     */
    @PostMapping("/addOrUpdateUser")
    @PreAuthorize("hasRole('ADMIN')")
    WebResult addUser(User paramUser, HttpServletRequest request) {
        String sexEnumStr = request.getParameter("sexEnumStr")
        String educationEnumStr = request.getParameter("educationEnumStr")
        String birthdayStr = request.getParameter("birthdayStr")
        String occupationDateStr = request.getParameter("occupationDateStr")
        String occupationEndDateStr = request.getParameter("occupationEndDateStr")

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
            if (paramUser.username != null || paramUser.password != null) {
                return WebResult.generateFalseWebResult("禁止修改用户名或密码！")
            }
            user.updaterId = securityService.getCurrentUser().id
        } else {
            //新增
            if (!(paramUser.username && paramUser.password)) {
                return WebResult.generateFalseWebResult("缺少必填项！")
            }
            User existUser = userRepository.findByUsernameAndDeleted(paramUser.username, 0)
            if (existUser) {
                return WebResult.generateFalseWebResult("用户名重复！")
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
        } catch(Exception e) {
            e.printStackTrace()
            return WebResult.generateTrueWebResult("系统错误！")
        }
        return WebResult.generateTrueWebResult("保存成功！")
    }


    /**
     * 获取当前用户
     * @return
     */
    @RequestMapping("/currentUser")
    WebResult getCurrentUser() {
        User user = securityService.getCurrentUser()
        WebResult.generateTrueWebResult([
                username: user.username,
                avatar  : user.avatar,
                nickName: user.nickName,
                email   : user.email,
                sexEnum : user.sexEnum?.getDesc(),
        ])
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
        def result = userService.formatPropertyToMap(user)
        return WebResult.generateTrueWebResult(result)
    }
}