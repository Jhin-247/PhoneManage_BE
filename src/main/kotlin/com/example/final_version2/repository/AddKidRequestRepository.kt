package com.example.final_version2.repository

import com.example.final_version2.model.AddKidRequest
import com.example.final_version2.model.AddParentRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AddKidRequestRepository: JpaRepository<AddKidRequest,Long> {

    @Query("select u from AddKidRequest u where u.requester.email = ?1 and u.child.email = ?2 order by u.time desc ")
    fun getAllRequest(requester: String, receiver: String): List<AddKidRequest>
}