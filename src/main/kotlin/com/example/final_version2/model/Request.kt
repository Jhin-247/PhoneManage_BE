package com.example.final_version2.model

import javax.persistence.*

@Entity
@Table
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
open class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id = 0L

    @ManyToOne
    @JoinColumn(name = "requester_id")
    var requester = Parent()

    var time: Long = 0L

    var action: Int = 0
}