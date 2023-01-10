package com.example.final_version2.model

import javax.persistence.*

@Entity
@Table
class AppSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id = 0L

    @ManyToOne
    @JoinColumn(name = "id_user")
    var user: User? = null

    @ManyToOne
    @JoinColumn(name = "app")
    var app: App? = null

    var isLock: Boolean = false

    var isLimited: Long = 0L

}