package com.voice.law.service

import com.voice.law.domain.User
import com.voice.law.mapper.UserMapper
import com.voice.law.util.SysConstant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.text.SimpleDateFormat

/**
 * 描述
 * @author zsd* @date 2020/1/7 4:25 下午
 */
@Service
class UserService {

    @Autowired
    UserMapper userMapper

    /**
     * 格式化user数据
     * @param user
     * @return
     */
    Map formatPropertyToMap(User user, boolean detail) {
        List<Map> roleList = userMapper.findRoleListByUserId(user.id)
        def result = [
                userId       : user.id,
                username     : user.username,
                nickName     : user.nickName,
                avatar       : user.avatar,
                sexEnum      : user.sexEnum ? [
                        key : user.sexEnum.toString(),
                        desc: user.sexEnum.getDesc()
                ] : null,
                createTime   : user.createTime ? SysConstant.yyyyMMddHHmmssSdf.format(user.createTime) : null,
                lastLoginTime: user.lastLoginTime ? SysConstant.yyyyMMddHHmmssSdf.format(user.lastLoginTime) : null,
                roleList     : roleList?.collect {
                    [
                            roleName   : it.role_name,
                            description: it.description
                    ]
                }
        ]

        if (detail) {
            result << [
                    name             : user.name,
                    phone            : user.phone,
                    email            : user.email,
                    userTypeEnum     : user.userTypeEnum ? [
                            key : user.userTypeEnum.toString(),
                            desc: user.userTypeEnum.getDesc()
                    ] : null,
                    age              : user.age,
                    birthday         : user.birthday ? SysConstant.yyyyMMddSdf.format(user.birthday) : null,
                    educationEnum    : user.educationEnum ? [
                            key : user.educationEnum.toString(),
                            desc: user.educationEnum.getDesc()
                    ] : null,
                    major            : user.major,
                    industry         : user.industry,
                    occupation       : user.occupation,
                    industryCode     : user.industryCode,
                    occupationCode   : user.occupationCode,
                    idCardNo         : user.idCardNo,
                    provinceCode     : user.provinceCode,
                    cityCode         : user.cityCode,
                    areaCode         : user.areaCode,
                    province         : user.province,
                    city             : user.city,
                    area             : user.area,
                    street           : user.street,
                    occupationDate   : user.occupationDate ? SysConstant.yyyyMMddSdf.format(user.occupationDate) : null,
                    occupationEndDate: user.occupationEndDate ? SysConstant.yyyyMMddSdf.format(user.occupationEndDate) : null,
                    adept            : user.adept,
                    briefIntroduction: user.briefIntroduction,
                    workExperience   : user.workExperience,
                    updateTime       : user.updateTime ? SysConstant.yyyyMMddHHmmssSdf.format(user.updateTime) : null,
            ]
        }

        return result
    }
}
