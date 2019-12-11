package com.voice.law.service

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
     * 校验登录
     * @param servletRequest
     * @return
     */
    boolean checkLogin(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies()
        for(Cookie cookie : cookies){
            if(cookie.getName() == SysConstant.LOGIN_COOKIE_NAME){
                String cookieVal = cookie.getValue()
                Object userCookie = redisTemplate.opsForValue().get(SysConstant.REDIS_CONSTANT + SysConstant.USER_COOKIE + cookie)
                if (!userCookie) {
                    return false
                }
                String userId = "1"
                Cookie cookie2 = new Cookie(cookieVal, userId)
                System.out.println(cookie2)
                cookie2.setMaxAge(30 * 60)
                response.addCookie(cookie2)
                return true
            } else {
                return false
            }
        }
    }
}
