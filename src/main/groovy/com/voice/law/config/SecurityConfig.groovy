package com.voice.law.config

import com.voice.law.filter.AccessDeniedHandler
import com.voice.law.filter.JwtTokenFilter
import com.voice.law.filter.TokenExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

import javax.annotation.Resource

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private TokenExceptionHandler tokenExceptionHandler
    @Resource
    private AccessDeniedHandler accessDeniedHandler
    @Resource
    private JwtTokenFilter jwtTokenFilter

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
        // 因为我们的token是无状态的，不需要跨站保护
                .csrf().disable()
        // 添加异常处理，以及访问禁止（无权限）处理
                .exceptionHandling().authenticationEntryPoint(tokenExceptionHandler).accessDeniedHandler(accessDeniedHandler).and()

        // 我们不再需要session了
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

        //定义拦截页面，所有api全部需要认证
                .authorizeRequests()
        //TODO 在此插入权限校验的请求路径
//                .antMatchers()
//                .permitAll()
                .anyRequest().authenticated()

        //最后，我们定义 filter，用来替换原来的UsernamePasswordAuthenticationFilter
        httpSecurity.addFilterAt(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
    }

    @Override
    void configure(WebSecurity web) throws Exception {
        web.ignoring()
        // 让我们获取 token的api不走spring security的过滤器，大道开放
                .antMatchers(HttpMethod.POST, "/rest/user/login")
    }
}