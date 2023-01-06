package com.example.final_version2.model

import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table
class AddKidNotification : Notification() {

    @OneToOne
    @JoinColumn(name = "add_kid_request")
    var request = AddKidRequest()

}