package com.voice.law.domain

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.Temporal
import javax.persistence.TemporalType

/**
 * 描述
 * @author zsd* @date 2019/12/12 1:45 下午
 */
@MappedSuperclass
class BaseDomain {

    Integer creatorId //创建者id
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    Date createTime //创建时间
    Integer updaterId //修改者id
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date updateTime //修改时间
    Integer deleterId //删除者id
    Date deleteTime //删除时间
    @Column(nullable = false, columnDefinition = "0")
    Integer deleted //是否删除 0：未删除 1：已删除
}
