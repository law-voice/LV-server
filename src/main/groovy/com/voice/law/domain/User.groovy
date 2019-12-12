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

}
