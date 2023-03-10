package com.example.final_version2.model

import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table
class AddParentNotification : Notification() {

    @OneToOne
    @JoinColumn(name = "add_parent_request")
    var request = AddParentRequest()

}