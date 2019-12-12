package com.voice.law.domain

/**
 * 描述
 * @author zsd* @date 2019/12/12 1:45 下午
 */
class BaseDomain {

    Integer creatorId //创建者id
    Date createTime //创建时间
    Integer updaterId //修改者id
    Date updateTime //修改时间
    Integer deleterId //删除者id
    Date deleteTime //删除时间
    Integer deleted //是否删除 0：未删除 1：已删除
}
