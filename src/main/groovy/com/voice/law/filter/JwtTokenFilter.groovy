package com.voice.law.filter

import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.SerializerFeature
import com.voice.law.domain.AuthUser
import com.voice.law.util.JwtUtil
import com.voice.law.util.SpringUtil
import com.voice.law.util.SysConstant
import com.voice.law.util.WebResult
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * token校验过滤器
 * create by zsd
 */
class JwtTokenFilter extends OncePerRequestFilter {

    StringRedisTemplate redisTemplate = (StringRedisTemplate) SpringUtil.getBean(StringRedisTemplate.class)

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8")
        response.setContentType("text/json;charset=utf-8")
        String token = request.getHeader(SysConstant.ACCESS_TOKEN)

        //获取token，并且解析token，如果解析成功，则放入 SecurityContext
        if (token != null) {
            try {
                AuthUser authUser = JwtUtil.parseToken(token)
                //todo: 如果此处不放心解析出来的 authuser，可以再从数据库查一次，验证用户身份：

                //解析成功
                //token 不是 access token
                if (authUser.getOption() != SysConstant.ACCESS_TOKEN) {
                    response.getWriter().write(JSONObject.toJSONString(WebResult.generateUnTokenWebResult(), SerializerFeature.WriteMapNullValue))
                    return
                }
                //token 与 redis中存储的不一致 说明当前token已被更改，有可能是被刷新了 也有可能是在其他设备重新登录了
                String oldToken = redisTemplate.opsForValue().get(SysConstant.REDIS_KEY_USER_ACCESS_TOKEN + authUser.username)
                if (token != oldToken) {
                    response.getWriter().write(JSONObject.toJSONString(WebResult.generateUnTokenWebResult(), SerializerFeature.WriteMapNullValue))
                    return
                }

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    //我们依然使用原来filter中的token对象
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken)
                }
            } catch (Exception e) {
                logger.info("access_token解析失败，可能是伪造的或者该token已经失效了。")
                e.printStackTrace()
                response.getWriter().write(JSONObject.toJSONString(WebResult.generateUnTokenWebResult(), SerializerFeature.WriteMapNullValue))
                return
            }
        } else {
            response.getWriter().write(JSONObject.toJSONString(WebResult.generateUnTokenWebResult(), SerializerFeature.WriteMapNullValue))
            return
        }

        filterChain.doFilter(request, response)
    }
}
