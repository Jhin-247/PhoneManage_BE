package com.example.final_version2.repository

import com.example.final_version2.model.ItemClass
import com.example.final_version2.model.Teacher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ClassRepository : JpaRepository<ItemClass, Long> {

    @Query("select u from ItemClass u where u.teacher = ?1")
    fun findByTeacher(teacher: Teacher): List<ItemClass>

}