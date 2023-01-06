package com.example.final_version2.model

import javax.persistence.*

@Entity
@Table
class Child : User() {

    @ManyToOne
    @JoinColumn(name = "id_super_user_1")
    var superUser1: User? = null

    @ManyToOne
    @JoinColumn(name = "id_super_user_2")
    var superUser2: User? = null

}