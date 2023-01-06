package com.example.final_version2.model

import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table
class AddParentRequest: Request() {

    var isForce: Boolean = false

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    var partner = Parent()

}