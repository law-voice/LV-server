package com.voice.law.controller

import com.voice.law.util.SysConstant
import com.voice.law.util.WebResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import java.text.SimpleDateFormat

@RequestMapping("/index")
@RestController
class TestController {

    @RequestMapping("/welcome")
    def welcome() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        def result = [
                environment: [
                        frameWork : "Spring Boot 2.2.0",
                        language  : "Java 1.8.0_181",
                        dataSource: "Mysql 5.7",
                        cache     : "Redis 5.0",
                ],
                server     : [
                        name     : "law-voice",
                        startTime: sdf.format(SysConstant.startTime),
                        stat     : "running",
                ]
        ]
        return WebResult.generateTrueWebResult(result, "LAW VOICE SERVER IS RUNNING!")
    }
}
