package com.voice.law.service

import com.voice.law.domain.Area
import com.voice.law.domain.City
import com.voice.law.domain.Province
import com.voice.law.jpa.AreaRepository
import com.voice.law.jpa.CityRepository
import com.voice.law.jpa.ProvinceRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct

/**
 * 描述
 * @author zsd* @date 2020/1/15 5:47 下午
 */
@Service
class UtilService {

    @Autowired
    ProvinceRepository provinceRepository
    @Autowired
    CityRepository cityRepository
    @Autowired
    AreaRepository areaRepository

    private static List provinceCityArea3levelList
    private Logger logger = LoggerFactory.getLogger(UtilService.class)

    /**
     * 省市区三级列表数据 项目启动执行
     * @return
     */
    @PostConstruct
    List provinceCityArea3levelList() {
        def provinceMap, cityMap, areaMap
        if (!provinceCityArea3levelList) {
            logger.info("<===> 初始化省市区3级列表...")
            synchronized (UtilService.class) {
                provinceCityArea3levelList = []
                List<Province> provinceList = provinceRepository.findAllByOrderByCode()
                List<City> cityList = cityRepository.findAllByOrderByCode()
                List<Area> areaList = areaRepository.findAllByOrderByCode()

                provinceList.each { Province province ->
                    provinceMap = [
                            n : province.name, //n: name 最大化压缩数据大小
                            c : province.code, //c: code
                            _c: []             //_c: child
                    ]
                    cityList.each { City city ->
                        if (city.provinceCode == province.code) {
                            cityMap = [
                                    n : city.name,
                                    c : city.code,
                                    _c: []
                            ]
                            areaList.each { Area area ->
                                if (area.cityCode == city.code) {
                                    areaMap = [
                                            n: area.name,
                                            c: area.code
                                    ]
                                    cityMap._c << areaMap
                                }
                            }
                            provinceMap._c << cityMap
                        }
                    }
                    provinceCityArea3levelList << provinceMap
                }
                logger.info("<===> 初始化省市区3级列表完成！")
            }
        }
        return provinceCityArea3levelList
    }
}
