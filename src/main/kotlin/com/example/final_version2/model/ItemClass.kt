package com.example.final_version2.model

import java.util.*
import javax.persistence.*

@Entity
@Table
class ItemClass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id = 0L

    var timeCreated: Long = 0
    var classname: String = ""
    var description: String = ""
    var classId: UUID = UUID.randomUUID()
    var subject: String = ""
    var grade: Int = 0

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    var teacher: Teacher? = null
}