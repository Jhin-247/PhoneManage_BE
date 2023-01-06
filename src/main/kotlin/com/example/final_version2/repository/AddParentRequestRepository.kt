package com.example.final_version2.repository

import com.example.final_version2.model.AddParentRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AddParentRequestRepository : JpaRepository<AddParentRequest, Long> {

    @Query("select u from AddParentRequest u where u.requester.email = ?1 and u.partner.email = ?2 order by u.time desc ")
    fun getAllRequest(requester: String, receiver: String): List<AddParentRequest>

    @Query("select u from AddParentRequest u where u.requester.email = ?1 or u.partner.email = ?1 order by u.time desc ")
    fun getAllRequestByEmail(requester: String): List<AddParentRequest>

}