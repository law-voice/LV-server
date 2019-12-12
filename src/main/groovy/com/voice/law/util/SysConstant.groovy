package com.voice.law.util

/**
 * 描述
 * @author zsd* @date 2019/12/10 6:27 下午
 */
class SysConstant {

    public static Date startTime
    public static final Integer REDIS_LOGIN_COOKIE_TIME_OUT = 30 * 60 //redis登陆存储的cookie超时时间

    public static final String LOGIN_COOKIE_NAME = "LV_SECURITY_AUTH" //web登陆cookie name

    public static final String REDIS_CONSTANT = "LAW_VOICE:" //redis项目前缀
    public static final String USER_COOKIE = "SYS_USER_COOKIE:" //system user cookie 前缀
    public static final String USER_TOKEN = "USER_TOKEN:" //user token
}
