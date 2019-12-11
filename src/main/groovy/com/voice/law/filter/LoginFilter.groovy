package com.voice.law.filter


import com.alibaba.fastjson.JSONObject
import com.voice.law.service.SecurityService
import com.voice.law.util.WebResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 描述
 * @author zsd* @date 2019/12/11 4:00 下午
 */
@WebFilter(filterName = "loginFilter", urlPatterns = "/*")
@Component
class LoginFilter implements Filter {

    @Autowired
    SecurityService securityService

    @Override
    void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest
        HttpServletResponse response = (HttpServletResponse) servletResponse
        boolean login = securityService.checkLogin(request, response)
        if (login) {
            filterChain.doFilter(request, response)
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(JSONObject.toJSONString(new WebResult(403, "用户未登录！", [:])))
        }
    }

    @Override
    void destroy() {

    }
}
