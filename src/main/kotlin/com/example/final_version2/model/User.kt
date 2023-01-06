package com.example.final_version2.model

import javax.persistence.*

@Entity
@Table
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id = 0L
    var username = ""
    var role = 0
    var email = ""
    var password = ""
    var accessToken = ""
    var avatarUrl = ""
    var uid = ""
}