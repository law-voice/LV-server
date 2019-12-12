package com.voice.law.service

import com.voice.law.util.CookieUtil
import com.voice.law.util.SysConstant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

import javax.servlet.ServletRequest
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

/**
 * 描述 安全服务 service
 * @author zsd* @date 2019/12/11 9:33 上午
 */
@Service
class SecurityService {

    @Autowired
    RedisTemplate redisTemplate

    /**
     * 校验系统用户登录
     * @param servletRequest
     * @return
     */
    boolean checkSysUserLogin(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies()
        Cookie cookie = CookieUtil.getCookieByName(cookies, SysConstant.LOGIN_COOKIE_NAME)
        if (cookie) {
            String cookieVal = cookie.getValue()
            Object sysUserId = redisTemplate.opsForValue().get(SysConstant.REDIS_CONSTANT + SysConstant.USER_COOKIE + cookieVal)
            if (!sysUserId) {
                return false
            }
            Cookie cookie2 = new Cookie(SysConstant.LOGIN_COOKIE_NAME, cookieVal)
            System.out.println(cookie2)
            cookie2.setMaxAge(SysConstant.REDIS_LOGIN_COOKIE_TIME_OUT)
            response.addCookie(cookie2)
            return true
        }
        return false
    }


}
