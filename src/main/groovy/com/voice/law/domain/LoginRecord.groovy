package com.voice.law.domain

import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

/**
 * 描述
 * @author zsd* @date 2019/12/11 7:03 下午
 */
@Entity
@Table(name = "login_record")
@DynamicInsert
@DynamicUpdate
class LoginRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id
    Integer userId
    @Temporal(TemporalType.TIMESTAMP)
    Date loginTime

}
