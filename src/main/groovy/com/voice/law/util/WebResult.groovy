package com.voice.law.util

/**
 * 描述 返回到客户端的数据结构
 * @author zsd* @date 2019/12/10 5:56 下午
 */
class WebResult {
    Integer code
    String msg
    Object data

    WebResult(Integer code, String msg, Object data) {
        this.code = code
        this.msg = msg
        this.data = data
    }

    static generateTrueWebResult(Object data) {
        return new WebResult(200, "SUCCESS", data)
    }

    static generateTrueWebResult(Object data, String msg) {
        return new WebResult(200, msg, data)
    }

    static generateFalseWebResult(Object data, String msg) {
        return new WebResult(500, msg, data)
    }

    static generateFalseWebResult(String msg) {
        return new WebResult(500, msg, null)
    }

    static generateUnTokenWebResult() {
        return new WebResult(401, "请求无效，没有有效token！", null)
    }

    static generateUnRoleWebResult() {
        return new WebResult(403, "权限不足！", null)
    }
}
