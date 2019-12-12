package com.voice.law.filter


import com.alibaba.fastjson.JSONObject
import com.voice.law.service.SecurityService
import com.voice.law.util.WebResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.annotation.Order
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
 * 描述 后台用户登录过滤器
 * @author zsd* @date 2019/12/11 4:00 下午
 */
@WebFilter(filterName = "loginFilter", urlPatterns = "/*")
@Component
@Order(1)
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
        response.setCharacterEncoding("UTF-8")
        response.setContentType("text/json;charset=utf-8")

        String servletPath = request.getServletPath() // /rest/sysUser/login
        if (servletPath.startsWith("/rest/")) {
            doRestFilter(request, response, filterChain)
        } else if (servletPath.startsWith("/api/")) {
            doApiFilter(request, response, filterChain)
        } else {
            filterChain.doFilter(request, response)
        }
    }

    /**
     * rest登录过滤器
     * @param request
     * @param response
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    void doRestFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        def unFilterPath = [
                "/rest/sysUser/login",
                "/rest/sysUser/register",
        ]

        boolean check = true
        unFilterPath.each {
            if (request.getRequestURI().endsWith(it)) {
                check = false
            }
        }

        if (check) {
            boolean login = securityService.checkSysUserLogin(request, response)
            if (login) {
                filterChain.doFilter(request, response)
            } else {
                response.getWriter().write(JSONObject.toJSONString(new WebResult(403, "用户未登录！", [:])))
            }
        } else {
            filterChain.doFilter(request, response)
        }
    }

    /**
     * api登录过滤器
     * @param request
     * @param response
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    void doApiFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    void destroy() {

    }
}
