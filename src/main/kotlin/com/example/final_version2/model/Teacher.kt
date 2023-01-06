package com.example.final_version2.model

import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table
class Teacher : User() {

    var subject: String = ""
    var grade: Int = 0

}