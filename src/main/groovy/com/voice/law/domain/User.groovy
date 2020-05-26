package com.voice.law.domain

import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate

import javax.persistence.*

/**
 * 描述
 * @author zsd* @date 2019/12/11 7:03 下午
 */
@Entity
@Table(name = "user")
@DynamicInsert
@DynamicUpdate
class User extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id

    @Column(nullable = false)
    String username //用户名
    @Column(nullable = false)
    String password //密码
    String slat //用户密码MD5加密盐值
    String name //姓名
    String nickName //昵称
    String avatar //头像
    String phone //手机号
    String email //邮箱
    @Enumerated(EnumType.STRING)
    UserTypeEnum userTypeEnum //用户分类 app web
    @Enumerated(EnumType.STRING)
    SexEnum sexEnum //性别
    Integer age //年龄
    Date birthday //生日
    @Enumerated(EnumType.STRING)
    EducationEnum educationEnum //学历
    String major //专业
    String industry //行业
    String occupation //职业
    String industryCode //行业code
    String occupationCode //职业code
    String idCardNo //身份证号
    String provinceCode //省code
    String cityCode //市code
    String areaCode //区code
    String province //省
    String city //市
    String area //区
    String street //街道
    Date occupationDate //职业年限
    Date occupationEndDate //职业年限结束时间
    String adept //擅长领域
    @Column(length = 500)
    String briefIntroduction //简介 长度500
    @Column(length = 500)
    String workExperience //工作经验 长度500
    Date lastLoginTime //最后一次登录时间
}

/**
 * 用户分类
 */
enum UserTypeEnum {
    APP(0, "app用户"),
    WEB(1, "web用户"),

    private Integer code = null
    private String desc = null

    UserTypeEnum(Integer code, String desc) {
        this.code = code
        this.desc = desc
    }

    String getCode() {
        return code
    }

    String getDesc() {
        return desc
    }

    static UserTypeEnum getEnum(Integer code) {
        if (null == code) {
            return null
        }
        for (UserTypeEnum a : UserTypeEnum.values()) {
            if (a.code == code) {
                return a
            }
        }
        throw new IllegalArgumentException("No enum code '" + code + "'. " + UserTypeEnum.class)
    }
}

/**
 * 性别
 */
enum SexEnum {
    FEMALE(0, "女"),
    MALE(1, "男"),

    private Integer code = null
    private String desc = null

    SexEnum(Integer code, String desc) {
        this.code = code
        this.desc = desc
    }

    String getCode() {
        return code
    }

    String getDesc() {
        return desc
    }

    static SexEnum getEnum(Integer code) {
        if (null == code) {
            return null
        }
        for (SexEnum a : SexEnum.values()) {
            if (a.code == code) {
                return a
            }
        }
        throw new IllegalArgumentException("No enum code '" + code + "'. " + SexEnum.class)
    }
}



/**
 * 学历
 */
enum EducationEnum {
    HIGH_SCHOOL_AND_BELOW(0, "高中及以下"),
    JUNIOR_COLLEGE(1, "大专"),
    BACHELOR(2, "本科"),
    MASTER(3, "硕士"),
    DOCTOR_AND_ABOVE(4, "博士及以上"),
//    OTHER(5, "其他"),

    private Integer code = null
    private String desc = null

    EducationEnum(Integer code, String desc) {
        this.code = code
        this.desc = desc
    }

    String getCode() {
        return code
    }

    String getDesc() {
        return desc
    }

    static EducationEnum getEnum(Integer code) {
        if (null == code) {
            return null
        }
        for (EducationEnum a : EducationEnum.values()) {
            if (a.code == code) {
                return a
            }
        }
        throw new IllegalArgumentException("No enum code '" + code + "'. " + EducationEnum.class)
    }
}
