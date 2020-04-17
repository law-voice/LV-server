package com.voice.law.controller

import com.voice.law.service.UtilService
import com.voice.law.util.WebResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 描述 工具controller
 * @author zsd* @date 2020/1/15 4:45 下午
 */
@RequestMapping("/util")
@RestController
class UtilController {

    @Autowired
    UtilService utilService

    /**
     * 省市区三级列表
     * @return
     */
    @GetMapping("/provinceCityArea3levelList")
    WebResult provinceCityArea3levelList() {
        return WebResult.generateTrueWebResult([list: utilService.provinceCityArea3levelList()])
    }
}
