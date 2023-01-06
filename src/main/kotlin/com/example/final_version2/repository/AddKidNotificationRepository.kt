package com.example.final_version2.repository

import com.example.final_version2.model.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AddKidNotificationRepository: JpaRepository<AddKidNotification,Long> {

    @Query("select u from AddKidNotification u where u.receiver = ?1 and u.request = ?2")
    fun getLastAddKidRequest(receiver: Child, request: AddKidRequest): Optional<AddKidNotification>

}