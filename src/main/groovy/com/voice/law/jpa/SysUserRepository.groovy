package com.voice.law.jpa

import com.voice.law.domain.SysUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


/**
 * 描述
 * @author zsd* @date 2019/12/11 7:33 下午
 */
@Repository
interface SysUserRepository extends JpaRepository<SysUser, Integer>{

}