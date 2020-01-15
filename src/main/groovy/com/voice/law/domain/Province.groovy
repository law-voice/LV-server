package com.voice.law.domain

import javax.persistence.*

/**
 * 描述 省
 * @author zsd* @date 2020/1/2 2:31 下午
 */
@Entity
@Table(name = "province")
class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id

    String name
    String code

}
