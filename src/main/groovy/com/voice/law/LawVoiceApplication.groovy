package com.voice.law

import com.voice.law.util.SysConstant
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EntityScan(basePackages = "com.voice.law.domain")
@EnableJpaRepositories(basePackages = "com.voice.law.jpa")
class LawVoiceApplication {

    static void main(String[] args) {
        SysConstant.startTime = new Date()
        SpringApplication.run(LawVoiceApplication, args)
        println "-" * 48 + " LAW VOICE SERVER IS RUNNING! " + "-" * 48
    }
}
