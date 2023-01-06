package com.example.final_version2.repository

import com.example.final_version2.model.User
import com.example.final_version2.model.UserRelationshipRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRelationshipRepository : JpaRepository<UserRelationshipRequest, Long> {

    @Query("select u from UserRelationshipRequest u where u.requester = ?1 and u.receiver = ?2")
    fun getLastRequest(requester: User, receiver: User): Optional<UserRelationshipRequest>
}