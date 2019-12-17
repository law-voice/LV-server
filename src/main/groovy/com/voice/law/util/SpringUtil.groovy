package com.voice.law.util

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * 描述
 * @author zsd* @date 2019/12/16 4:37 下午
 */

@Component
class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext

    @Override
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext
        }
    }

    static ApplicationContext getApplicationContext() {
        return applicationContext
    }

    static Object getBean(String name) {
        return getApplicationContext().getBean(name)
    }

    static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz)
    }

    static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz)
    }
}
