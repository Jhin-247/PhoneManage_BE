package com.example.final_version2.repository

import com.example.final_version2.model.App
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AppRepository : JpaRepository<App, Int> {

    @Query("select u from App u where u.packageName = ?1")
    fun getAppByAppPackage(appPackage: String): Optional<App>

}