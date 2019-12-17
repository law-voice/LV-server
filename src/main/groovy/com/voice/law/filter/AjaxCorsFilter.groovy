//package com.voice.law.filter
//
//import org.springframework.core.Ordered
//import org.springframework.core.annotation.Order
//import org.springframework.stereotype.Component
//import org.springframework.web.cors.CorsConfiguration
//import org.springframework.web.cors.CorsConfigurationSource
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource
//import org.springframework.web.filter.CorsFilter
//
///**
// * 描述 开启跨域请求
// * @author zsd* @date 2019/12/12 10:11 上午
// */
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//class AjaxCorsFilter extends CorsFilter {
//    AjaxCorsFilter() {
//        super(configurationSource())
//    }
//
//    private static UrlBasedCorsConfigurationSource configurationSource() {
//        CorsConfiguration corsConfig = new CorsConfiguration()
//        List<String> allowedHeaders = Arrays.asList("x-auth-token", "content-type", "X-Requested-With", "XMLHttpRequest")
//        List<String> exposedHeaders = Arrays.asList("x-auth-token", "content-type", "X-Requested-With", "XMLHttpRequest")
//        List<String> allowedMethods = Arrays.asList("POST", "GET", "DELETE", "PUT", "OPTIONS")
//        List<String> allowedOrigins = Arrays.asList("*")
//        corsConfig.setAllowedHeaders(allowedHeaders)
//        corsConfig.setAllowedMethods(allowedMethods)
//        corsConfig.setAllowedOrigins(allowedOrigins)
//        corsConfig.setExposedHeaders(exposedHeaders)
//        corsConfig.setMaxAge(36000L)
//        corsConfig.setAllowCredentials(true)
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", corsConfig)
//        return source
//    }
//}
