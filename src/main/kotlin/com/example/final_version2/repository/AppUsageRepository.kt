package com.example.final_version2.repository

import com.example.final_version2.model.AppUsage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppUsageRepository: JpaRepository<AppUsage,Long> {
}