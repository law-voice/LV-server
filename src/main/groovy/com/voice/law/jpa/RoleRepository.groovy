package com.voice.law.jpa

import com.voice.law.domain.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(String roleName)




}