package com.example.final_version2.repository

import com.example.final_version2.model.JoinClassRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface JoinClassRequestRepository: JpaRepository<JoinClassRequest,Long> {

    @Query("SELECT u from JoinClassRequest u where u.classToJoin.id = ?1")
    fun findRequestByClassId(classId: Long): List<JoinClassRequest>

}