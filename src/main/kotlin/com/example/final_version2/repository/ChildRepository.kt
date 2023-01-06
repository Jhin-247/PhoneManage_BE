package com.example.final_version2.repository

import com.example.final_version2.model.Child
import com.example.final_version2.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChildRepository : JpaRepository<Child, Long> {

    @Query("select u from Child u where u.email = ?1")
    fun searchKidByEmail(email: String): Optional<Child>
}