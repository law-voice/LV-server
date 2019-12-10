package com.voice.law

import com.voice.law.util.SysConstant
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class LawVoiceApplication {

    static void main(String[] args) {
        SysConstant.startTime = new Date()
        SpringApplication.run(LawVoiceApplication, args)
    }
}
