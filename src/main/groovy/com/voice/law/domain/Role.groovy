package com.voice.law.domain

import javax.persistence.*

@Entity
@Table(name = "role")
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id

    String roleName
    String description
}
