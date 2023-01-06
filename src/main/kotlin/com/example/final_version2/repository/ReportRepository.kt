package com.example.final_version2.repository

import com.example.final_version2.model.Report
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository: JpaRepository<Report,Long> {
}