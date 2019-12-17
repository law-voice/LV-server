package com.voice.law.controller.rest

import com.voice.law.domain.AuthUser
import com.voice.law.domain.Role
import com.voice.law.domain.User
import com.voice.law.jpa.RoleRepository
import com.voice.law.jpa.UserRepository
import com.voice.law.jpa.UserRoleRepository
import com.voice.law.service.SecurityService
import com.voice.law.util.JwtUtil
import com.voice.law.util.SysConstant
import com.voice.law.util.WebResult
import groovy.time.TimeCategory
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

    Logger logger = LoggerFactory.getLogger(this.class)

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    WebResult login(String username, String password) {
        User user = userRepository.findByUsername(username)
        if (user == null || !(user.password == password)) {
            return WebResult.generateFalseWebResult("用户名密码错误！")
        }
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
        try{
            AuthUser authUser = JwtUtil.parseToken(refreshToken)
            if (authUser.option != SysConstant.REFRESH_TOKEN) {
                return WebResult.generateUnTokenWebResult()
            }
            User user = userRepository.findByUsername(authUser.username)
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
    @PostMapping("/addUser")
    @PreAuthorize("hasRole('admin')")
    WebResult addUser() {

    }


    @RequestMapping("/current")
    WebResult getCurrentUser() {
        WebResult.generateTrueWebResult([
                username: securityService.getCurrentUser().username
        ])
    }

}