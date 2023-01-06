package com.example.final_version2.model

import javax.persistence.*

@Entity
@Table
class UserRelationshipRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id = 0L

    @ManyToOne
        @JoinColumn(name = "requester_id")
    var requester: User? = null

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    var receiver: User? = null

    var requestTime: Long = -1L

    var action: Int = 0

}