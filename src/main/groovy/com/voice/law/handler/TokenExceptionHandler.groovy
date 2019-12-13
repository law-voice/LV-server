package com.voice.law.handler

import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.SerializerFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.voice.law.util.WebResult
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * token异常处理
 * create by zsd
 */
@Component
class TokenExceptionHandler implements AuthenticationEntryPoint {

    @Override
    void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8")
        response.setContentType("text/json;charset=utf-8")
        response.getWriter().write(JSONObject.toJSONString(WebResult.generateUnTokenWebResult(), SerializerFeature.WriteMapNullValue))
    }
}