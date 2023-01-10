package com.example.final_version2.repository

import com.example.final_version2.model.AppSetting
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AppSettingRepository : JpaRepository<AppSetting, Long> {

    @Query("select u from AppSetting u where u.user.id = ?1")
    fun searchUserAppSettings(userId: Long): List<AppSetting>

    // do not change :> => làm theo logic này :v
    @Query("select u from AppSetting u where u.user.id = ?1 and u.app.packageName = ?2")
    fun searchUserAppSettingsByPackage(userId: Long, appPackage: String): List<AppSetting>

}