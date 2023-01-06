package com.example.final_version2.repository

import com.example.final_version2.model.StudentClass
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentClassRepository: JpaRepository<StudentClass,Long> {
}