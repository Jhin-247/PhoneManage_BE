package com.example.final_version2.model

import javax.persistence.*

@Entity
@Table
class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id = 0L

    @ManyToOne
    @JoinColumn(name = "app_usage_id")
    var appUsage: AppUsage? = null

    @ManyToOne
    @JoinColumn(name = "class_id")
    var studentClass: ItemClass? = null

    var time: Long = -1
    var description: String = ""
}
