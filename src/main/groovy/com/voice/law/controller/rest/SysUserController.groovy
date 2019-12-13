package com.voice.law.controller.rest


import com.voice.law.domain.User
import com.voice.law.jpa.SysUserRepository
import com.voice.law.util.CookieUtil
import com.voice.law.util.SysConstant
import com.voice.law.util.WebResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.concurrent.TimeUnit

/**
 * 描述
 * @author zsd* @date 2019/12/11 5:17 下午
 */
@RestController
@RequestMapping("/rest/sysUser")
class SysUserController {

    @Autowired
    SysUserRepository sysUserRepository
    @Autowired
    RedisTemplate redisTemplate

    @RequestMapping("/login")
    def login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        User sysUser = sysUserRepository.findByUsernameAndPassword(username, password)
        if (sysUser) {
            Cookie oldCookie = CookieUtil.getCookieByName(request.getCookies(), SysConstant.LOGIN_COOKIE_NAME)
            if (oldCookie) {
                Object sysUserId = redisTemplate.opsForValue().get(SysConstant.REDIS_CONSTANT + SysConstant.USER_COOKIE + oldCookie.getValue())
                if (sysUserId && sysUserId.toString() == sysUser.id.toString()) {
                    return WebResult.generateFalseWebResult("您已登录，请勿重复登陆！")
                }
            }

            String loginCookie = UUID.randomUUID().toString().replaceAll("-", "")
            redisTemplate.opsForValue().set(SysConstant.REDIS_CONSTANT + SysConstant.USER_COOKIE + loginCookie, sysUser.id.toString(), SysConstant.REDIS_LOGIN_COOKIE_TIME_OUT, TimeUnit.SECONDS)

            Cookie cookie = new Cookie(SysConstant.LOGIN_COOKIE_NAME, loginCookie)
            cookie.setMaxAge(SysConstant.REDIS_LOGIN_COOKIE_TIME_OUT)
            response.addCookie(cookie)

            return WebResult.generateTrueWebResult([
                    sysUser: [
                            id      : sysUser.id,
                            username: sysUser.username,
                            nickName: sysUser.nickName ? sysUser.nickName : ""
                    ]
            ], "登陆成功！")
        } else {
            return WebResult.generateFalseWebResult("用户名或密码错误！")
        }
    }

    @RequestMapping("/logout")
    def logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies()
        Cookie cookie = CookieUtil.getCookieByName(cookies, SysConstant.LOGIN_COOKIE_NAME)
        if (cookie) {
            Object sysUserId = redisTemplate.opsForValue().get(SysConstant.REDIS_CONSTANT + SysConstant.USER_COOKIE + cookie.getValue())
            if (sysUserId) {
                redisTemplate.delete(SysConstant.REDIS_CONSTANT + SysConstant.USER_COOKIE + cookie.getValue())
            }
        }
        return WebResult.generateTrueWebResult([:], "退出登陆成功！")
    }

    @RequestMapping("/register")
    def register(User sysUser, String code) {


        print(sysUser.username + "=========")
        print(sysUser.password + "=========")
        sysUser.creatorId = 1
        sysUserRepository.saveAndFlush(sysUser)

    }
}
