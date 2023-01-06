package com.example.final_version2.repository

import com.example.final_version2.model.Parent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ParentRepository : JpaRepository<Parent, Long> {

    @Query("select u from Parent u where u.email = ?1")
    fun searchParentByEmail(email: String): Optional<Parent>
}