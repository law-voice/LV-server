package com.voice.law;

import com.voice.law.util.SysConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class LawVoiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LawVoiceApplication.class, args);
        SysConstant.startTime = new Date();
    }

}
