package com.voice.law.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/test")
@RestController
class TestController {

    @RequestMapping("/1")
    def test() {
        [
                name: "join"
        ]
    }
}
