package com.voice.law.controller.rest

import com.voice.law.domain.AuthUser
import com.voice.law.util.WebResult
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/permission")
class PermissionController {

    @GetMapping("/normal")
    WebResult loginTest(@AuthenticationPrincipal AuthUser authUser) {
        return WebResult.generateTrueWebResult("你成功访问了该api，这代表你已经登录，你是： " + authUser, "")
    }

    @GetMapping("/role")
    @PreAuthorize("hasRole('ADMIN')")
    WebResult loginTest() {
        return WebResult.generateTrueWebResult("你成功访问了需要有 ADMIN 角色的api。")
    }
}