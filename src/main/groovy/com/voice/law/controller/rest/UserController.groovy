package com.voice.law.controller.rest

import com.voice.law.domain.AuthUser
import com.voice.law.domain.User
import com.voice.law.jpa.RoleRepository
import com.voice.law.jpa.UserRepository
import com.voice.law.jpa.UserRoleRepository
import com.voice.law.service.SecurityService
import com.voice.law.util.JwtUtil
import com.voice.law.util.SysConstant
import com.voice.law.util.WebResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

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
            e.printStackTrace()
            return WebResult.generateUnTokenWebResult()
        }
    }

    @RequestMapping("/current")
    WebResult getCurrentUser() {
        WebResult.generateTrueWebResult([
                username: securityService.getCurrentUser().username
        ])
    }
}