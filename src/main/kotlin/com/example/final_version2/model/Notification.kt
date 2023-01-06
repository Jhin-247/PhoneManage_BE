package com.example.final_version2.model

import javax.persistence.*

@Entity
@Table
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
open class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id = 0L

    var notificationType: Int = -1

    @ManyToOne()
    @JoinColumn(name = "receiver")
    var receiver: User? = null

    var time: Long = -1

    var isRead: Boolean = false

    var content: String = ""

}