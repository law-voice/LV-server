package com.voice.law.domain

import javax.persistence.*

@Entity
@Table(name = "user_role")
class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id
    Integer userId
    Integer roleId
}
