package com.voice.law.util

import java.text.SimpleDateFormat

/**
 * 描述
 * @author zsd* @date 2019/12/10 6:27 下午
 */
class SysConstant {

    public static Date startTime
//    public static final Integer REDIS_LOGIN_COOKIE_TIME_OUT = 30 * 60 //redis登陆存储的cookie超时时间
//
//    public static final String LOGIN_COOKIE_NAME = "LV_SECURITY_AUTH" //web登陆cookie name
//
//    public static final String REDIS_CONSTANT = "LAW_VOICE:" //redis项目前缀
//    public static final String USER_COOKIE = "SYS_USER_COOKIE:" //system user cookie 前缀
//    public static final String USER_TOKEN = "USER_TOKEN:" //user token

    public static final SimpleDateFormat yyyyMMddSdf = new SimpleDateFormat("yyyy-MM-dd")
    public static final SimpleDateFormat yyyyMMddHHmmssSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss"

    public static final String  ACCESS_TOKEN = "access_token" //认证token
    public static final String  REFRESH_TOKEN = "refresh_token" //刷新token

    public static final String REDIS_KEY_USER_ACCESS_TOKEN = "USER_ACCESS_TOKEN:" //认证token redis key
    public static final String REDIS_KEY_USER_REFRESH_TOKEN = "USER_REFRESH_TOKEN:" //刷新token redis key

    public static final Integer APP_ACCESS_TOKEN_TIMEOUT = 60 * 60 * 1000 //app用户 认证token超时时间 60min
    public static final Integer APP_REFRESH_TOKEN_TIMEOUT = 120 * 60 * 1000 //app用户 刷新token超时时间 120min
    public static final Integer REST_ACCESS_TOKEN_TIMEOUT = 15 * 24 * 60 * 60 * 1000 //后台用户 认证token超时时间 15days
    public static final Integer REST_REFRESH_TOKEN_TIMEOUT = 30 * 24 * 60 * 60 * 1000 //后台用户 刷新token超时时间 30days
}
