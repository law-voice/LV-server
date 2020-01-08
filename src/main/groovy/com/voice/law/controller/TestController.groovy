package com.voice.law.controller

import com.voice.law.util.SysConstant
import com.voice.law.util.WebResult
import org.springframework.boot.SpringApplication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import java.awt.Component
import java.text.SimpleDateFormat
import java.util.stream.Stream

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
                        SystemVersion: "CentOS Linux release 7.7.1908 (Core)",
                ],
                Server     : [
                        Name     : "law-voice",
                        StartTime: sdf.format(SysConstant.startTime),
                        Status   : "running",
                ],
                JavaInfo   : [
                        javaInfo()
                ]
        ]
        return WebResult.generateTrueWebResult(result, "LAW VOICE SERVER IS RUNNING!")
    }

    static List javaInfo() {
        def map = [
                [key: "Java 运行时环境版本", value: System.getProperty("java.version")],
                [key: "Java 运行时环境供应商", value: System.getProperty("java.vendor")],
                [key: "Java 供应商的 URL", value: System.getProperty("java.vendor.url")],
                [key: "Java 安装目录", value: System.getProperty("java.home")],
                [key: "Java 虚拟机规范版本", value: System.getProperty("java.vm.specification.version")],
                [key: "Java 虚拟机规范供应商", value: System.getProperty("java.vm.specification.vendor")],
                [key: "Java 虚拟机规范名称", value: System.getProperty("java.vm.specification.name")],
                [key: "Java 虚拟机实现版本", value: System.getProperty("java.vm.version")],
                [key: "Java 虚拟机实现供应商", value: System.getProperty("java.vm.vendor")],
                [key: "Java 虚拟机实现名称", value: System.getProperty("java.vm.name")],
                [key: "Java 运行时环境规范版本", value: System.getProperty("java.specification.version")],
                [key: "Java 运行时环境规范供应商", value: System.getProperty("java.specification.vendor")],
                [key: "Java 运行时环境规范名称", value: System.getProperty("java.specification.name")],
                [key: "Java 类格式版本号", value: System.getProperty("java.class.version")],
//                [key: "Java 类路径", value: System.getProperty("java.class.path")],
                [key: "加载库时搜索的路径列表", value: System.getProperty("java.library.path")],
                [key: "默认的临时文件路径", value: System.getProperty("java.io.tmpdir")],
                [key: "要使用的 JIT 编译器的名称", value: System.getProperty("java.compiler")],
                [key: "一个或多个扩展目录的路径", value: System.getProperty("java.ext.dirs")],
                [key: "操作系统的名称", value: System.getProperty("os.name")],
                [key: "操作系统的架构", value: System.getProperty("os.arch")],
                [key: "操作系统的版本", value: System.getProperty("os.version")],
                [key: "文件分隔符（在 UNIX 系统中是“/”）", value: System.getProperty("file.separator")],
                [key: "路径分隔符（在 UNIX 系统中是“:”）", value: System.getProperty("path.separator")],
                [key: "行分隔符（在 UNIX 系统中是“/n”）", value: System.getProperty("line.separator")],
                [key: "用户的账户名称", value: System.getProperty("user.name")],
                [key: "用户的主目录", value: System.getProperty("user.home")],
                [key: "用户的当前工作目录", value: System.getProperty("user.dir")],
        ]
        return map
    }
}
