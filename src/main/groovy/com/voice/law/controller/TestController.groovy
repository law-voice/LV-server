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
                Environment: [
                        Engine       : "Spring Boot 2.2.0",
                        Language     : "Java 1.8.0_181",
                        DataSource   : "Mysql 5.7",
                        Cache        : "Redis 5.0",
                        SystemVersion: "CentOS Linux release 7.7.1908 (Core)"
                ],
                Server     : [
                        Name     : "law-voice",
                        StartTime: sdf.format(SysConstant.startTime),
                        Status   : "running",
                ]
        ]
        return WebResult.generateTrueWebResult(result, "LAW VOICE SERVER IS RUNNING!")
    }
}
