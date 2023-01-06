package com.example.final_version2.model

import javax.persistence.*

@Entity
@Table
class App {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id = 0L
    var packageName: String = ""
    var appName: String = ""
}