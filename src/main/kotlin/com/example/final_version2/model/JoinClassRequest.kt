package com.example.final_version2.model

import javax.persistence.*

@Entity
@Table
class JoinClassRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id = 0L

    @ManyToOne
    @JoinColumn(name = "requester_id")
    var requester = Child()

    @ManyToOne
    @JoinColumn(name = "class_id")
    var classToJoin = ItemClass()

    var time: Long = 0L

}