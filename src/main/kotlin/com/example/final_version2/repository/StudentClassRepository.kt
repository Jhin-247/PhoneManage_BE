package com.example.final_version2.repository

import com.example.final_version2.model.StudentClass
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StudentClassRepository : JpaRepository<StudentClass, Long> {
    @Query("select u from StudentClass u where u.child.id = ?1 and u.joinClass.id = ?2")
    fun findStudentClassByStudentAndClass(studentId: Long, classId: Long): Optional<StudentClass>

}