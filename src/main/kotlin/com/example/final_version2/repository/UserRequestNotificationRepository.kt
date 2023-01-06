package com.example.final_version2.repository

import com.example.final_version2.model.RequestUserNotification
import com.example.final_version2.model.UserRelationshipRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRequestNotificationRepository : JpaRepository<RequestUserNotification, Long> {

    @Query("select u from RequestUserNotification u where u.userRelationshipRequest = ?1")
    fun getLastNotificationRequest(request: UserRelationshipRequest): Optional<RequestUserNotification>
}