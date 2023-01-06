package com.example.final_version2.repository

import com.example.final_version2.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {

    @Query("select u from User u where u.email = ?1")
    fun searchUserByEmail(email: String): Optional<User>
}