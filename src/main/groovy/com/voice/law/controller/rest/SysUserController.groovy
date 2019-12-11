package com.voice.law.controller.rest

import com.voice.law.domain.SysUser
import com.voice.law.jpa.SysUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 描述
 * @author zsd* @date 2019/12/11 5:17 下午
 */
@RestController
@RequestMapping("/sysUser")
class SysUserController {

    @Autowired
    SysUserRepository sysUserRepository

    @RequestMapping("login")
    def login(String username, String password, String code) {

    }

    @RequestMapping("/register")
    def register(SysUser sysUser) {
        print(sysUser.username + "=========")
        print(sysUser.password + "=========")
        sysUserRepository.saveAndFlush(sysUser)
    }
}
