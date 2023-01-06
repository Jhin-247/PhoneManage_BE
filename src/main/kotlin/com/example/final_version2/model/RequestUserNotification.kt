package com.example.final_version2.model

import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table
class RequestUserNotification : Notification() {

    @OneToOne
    @JoinColumn(name = "request_id")
    var userRelationshipRequest: UserRelationshipRequest? = null

}