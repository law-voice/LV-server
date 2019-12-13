package com.voice.law.handler

import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.SerializerFeature
import com.voice.law.util.WebResult
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 权限认证
 * create by zsd
 */
@Component
class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    @Override
    void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8")
        response.setContentType("text/json;charset=utf-8")
        response.getWriter().write(JSONObject.toJSONString(WebResult.generateUnRoleWebResult(), SerializerFeature.WriteMapNullValue))
    }
}
