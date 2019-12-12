package com.voice.law.controller.rest

import com.voice.law.domain.Role
import com.voice.law.domain.User
import com.voice.law.domain.UserRole
import com.voice.law.jpa.RoleRepository
import com.voice.law.jpa.SysUserRepository
import com.voice.law.jpa.UserRoleRepository
import com.voice.law.util.JwtUtil
import com.voice.law.util.WebResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/user")
class UserController {
    @Autowired
    private SysUserRepository sysUserRepository
    @Autowired
    private RoleRepository roleRepository
    @Autowired
    UserRoleRepository userRoleRepository

    @PostMapping("/login")
    WebResult login(String username, String password) {
        User user = sysUserRepository.findByUsername(username)

        if (user == null || !(user.password == password)) {
            return WebResult.generateFalseWebResult("用户名密码错误！")
        }

        List<UserRole> userRoleList = userRoleRepository.findByUserId(user.id)
        List<Integer> roleIdList = userRoleList.collect {
            it.roleId
        }
        List<Role> roleList = roleRepository.findAllById(roleIdList)

        return WebResult.generateTrueWebResult(
                [
                        token: JwtUtil.generateToken(username, roleList)
                ],
                "登陆成功！"
        )
    }
}