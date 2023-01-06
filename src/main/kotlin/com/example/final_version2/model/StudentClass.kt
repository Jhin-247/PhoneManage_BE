package com.example.final_version2.model

import javax.persistence.*

@Entity
@Table
class StudentClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id = 0L

    @ManyToOne
    @JoinColumn(name = "id_student")
    var child: Child? = null

    @ManyToOne
    @JoinColumn(name = "id_class")
    var joinClass: ItemClass? = null

    var isActive: Boolean = true

    var isBanned: Boolean = false
}