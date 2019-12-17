package com.voice.law.service

import com.voice.law.domain.Role
import com.voice.law.domain.User
import com.voice.law.domain.UserRole

import com.voice.law.domain.UserTypeEnum
import com.voice.law.jpa.RoleRepository
import com.voice.law.jpa.UserRepository
import com.voice.law.jpa.UserRoleRepository

import com.voice.law.util.CookieUtil
import com.voice.law.util.JwtUtil
import com.voice.law.util.SysConstant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.text.SimpleDateFormat

/**
 * 描述 安全服务 service
 * @author zsd* @date 2019/12/11 9:33 上午
 */
@Service
class SecurityService {

    @Autowired
    StringRedisTemplate redisTemplate
    @Autowired
    UserRepository userRepository
    @Autowired
    UserRoleRepository userRoleRepository
    @Autowired
    RoleRepository roleRepository

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

    /**
     * 获取当前用户
     * @return
     */
    User getCurrentUser() {
        def authentication = SecurityContextHolder.getContext().authentication
        def principal = authentication.principal
        User user = null
        if (principal) {
            user = userRepository.findByUsername(principal.username)
        }
        return user
    }

    /**
     * 生成用户token
     * @param user
     * @return
     */
    Map generateAuthTokenInfo(User user) {
        List<UserRole> userRoleList = userRoleRepository.findByUserId(user.id)
        List<Integer> roleIdList = userRoleList.collect {
            it.roleId
        }
        List<Role> roleList = roleRepository.findAllById(roleIdList)

        Date accessTokenTimeout = new Date(System.currentTimeMillis() + 15 * 60 * 1000) //15 min
        Date refreshTokenTimeout = new Date(System.currentTimeMillis() + 30 * 60 * 1000) //30 min

        if (user.userTypeEnum == UserTypeEnum.APP) {
            accessTokenTimeout = new Date(System.currentTimeMillis() + 15 * 24 * 60 * 60 * 1000) //15 days
            refreshTokenTimeout = new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000) //30 days
        }

        SimpleDateFormat sdf = new SimpleDateFormat(SysConstant.yyyyMMddHHmmss)

        String accessToken = JwtUtil.generateToken(user.username, SysConstant.ACCESS_TOKEN, roleList, accessTokenTimeout)
        String refreshToken = JwtUtil.generateToken(user.username, SysConstant.REFRESH_TOKEN, roleList, refreshTokenTimeout)
        //报存用户token
        redisTemplate.opsForValue().set(SysConstant.REDIS_KEY_USER_ACCESS_TOKEN + user.username, accessToken)
        redisTemplate.opsForValue().set(SysConstant.REDIS_KEY_USER_REFRESH_TOKEN + user.username, refreshToken)

        return [
                access : [
                        (SysConstant.ACCESS_TOKEN): accessToken,
                        time_out                  : sdf.format(accessTokenTimeout),
                ],
                refresh: [
                        (SysConstant.REFRESH_TOKEN):refreshToken,
                        time_out                   : sdf.format(refreshTokenTimeout),
                ]
        ]
    }
}
