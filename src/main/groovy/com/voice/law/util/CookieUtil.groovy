package com.voice.law.util

import javax.servlet.http.Cookie

class CookieUtil {

    static getCookieByName(Cookie[] cookies, String cookieName) {
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName() == cookieName) {
                return cookies[i]
            }
        }
        return null
    }
}
