package com.example.final_version2.model

import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table
class Parent : User() {

    var type: Int = -1
    var description = ""

//    @OneToOne
//    @JoinColumn(name = "partner_id")
//    var user: User? = null

    var partnerEmail: String = ""

}