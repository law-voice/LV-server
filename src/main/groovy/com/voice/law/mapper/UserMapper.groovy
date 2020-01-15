package com.voice.law.mapper

import org.apache.ibatis.annotations.Mapper

/**
 * 描述
 * @author zsd* @date 2020/1/13 2:56 下午
 */
@Mapper
interface UserMapper {
    /**
     * 用户权限列表
     * @param userId
     * @return
     */
    List<Map> findRoleListByUserId(Integer userId)

    /**
     * 用户列表
     * @param params
     * @return
     */
    List<Map> userList(Map params)
}