package com.voice.law.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table


/**
 * 描述
 * @author zsd* @date 2019/12/11 7:03 下午
 */
@Entity
@Table(name = "user")
class User extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id

    String username //用户名
    String password //密码
    String name //姓名
    String nickName //昵称
    String avatar //头像
    String phone //手机号
    String email //邮箱
    UserTypeEnum userTypeEnum //用户分类 app web

}

/**
 * 用户分类
 */
enum UserTypeEnum {
    APP(0, "app用户"),
    WEB(1, "web用户"),

    private Integer code = null
    private String desc = null

    UserTypeEnum(Integer code, String desc) {
        this.code = code
        this.desc = desc
    }

    String getCode() {
        return code
    }

    String getDesc() {
        return desc
    }

    static UserTypeEnum getEnum(Integer code) {
        if (null == code) {
            return null
        }
        for (UserTypeEnum a : UserTypeEnum.values()) {
            if (a.code == code) {
                return a
            }
        }
        throw new IllegalArgumentException("No enum code '" + code + "'. " + UserTypeEnum.class)
    }
}
