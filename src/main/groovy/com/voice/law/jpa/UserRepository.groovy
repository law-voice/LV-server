package com.voice.law.jpa

import com.voice.law.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


/**
 * 描述
 * @author zsd* @date 2019/12/11 7:33 下午
 */
@Repository
interface UserRepository extends JpaRepository<User, Integer>{

    User findByUsernameAndPassword(String username, String password)

    User findByUsername(String username)

    User findByUsernameAndDeleted(String username, Integer deleted)

    User findByIdAndDeleted(Integer id, Integer deleted)


}