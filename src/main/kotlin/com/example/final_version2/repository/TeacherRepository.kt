package com.example.final_version2.repository

import com.example.final_version2.model.Parent
import com.example.final_version2.model.Teacher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TeacherRepository: JpaRepository<Teacher,Long> {

    @Query("select u from Teacher u where u.email = ?1")
    fun searchTeacherByEmail(email: String): Optional<Teacher>

}