package com.example.final_version2.service

import com.example.final_version2.base.*
import com.example.final_version2.common.Constants
import com.example.final_version2.model.App
import com.example.final_version2.model.AppSetting
import com.example.final_version2.model.AppUsage
import com.example.final_version2.model.Report
import com.example.final_version2.repository.*
import com.example.final_version2.utils.FileUploadUtil
import com.example.final_version2.utils.getOneDayTime
import com.example.final_version2.utils.getTodayTimeStamp
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

@Service
class AppService {

    @Autowired
    private lateinit var appRepository: AppRepository

    @Autowired
    private lateinit var appUsageRepository: AppUsageRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var classRepository: ClassRepository

    @Autowired
    private lateinit var reportRepository: ReportRepository

    @Autowired
    private lateinit var appSettingRepository: AppSettingRepository

    fun uploadApp(appName: String, appPackage: String, appIcon: MultipartFile): BaseResponse<Boolean> {

        val existedApp = appRepository.getAppByAppPackage(appPackage)
        if (existedApp.isPresent) {
            return resultExisted()
        }
        val app = App()
        app.packageName = appPackage
        app.appName = appName
        appRepository.save(app)
        val appIconFileName = StringUtils.cleanPath("$appPackage.jpg")
        val uploadDir = "app-icons"
        FileUploadUtil.saveFile(uploadDir, appIconFileName, appIcon)
        return resultSuccess(true)
    }

    fun insertAppUsage(
        email: String,
        appPackage: String,
        appName: String,
        time: Long,
        action: Int
    ): BaseResponse<AppUsage> {
        val user = userRepository.searchUserByEmail(email).get()
        val app = appRepository.getAppByAppPackage(appPackage)
        if (app.isPresent) {
            val result = AppUsage()
            result.app = app.get()
            result.action = action
            result.time = time
            result.user = user
            val savedResult = appUsageRepository.save(result)
            return resultSuccess(savedResult)
        } else {
            val savedApp = saveApp(appName, appPackage)
            val result = AppUsage()
            result.app = savedApp
            result.action = action
            result.time = time
            result.user = user
            val savedResult = appUsageRepository.save(result)
            return resultNeedUpload(savedResult)
        }

    }

    fun getAppUsage(email: String, typeRequest: Int): BaseResponse<List<AppUsage>> {
        val allAppUsage = appUsageRepository.findAll()
        val result = mutableListOf<AppUsage>()

        val startTime: Long = when (typeRequest) {
            Constants.AppUsageQueryType.TODAY -> {
                getTodayTimeStamp()
            }

            Constants.AppUsageQueryType.LAST_7_DAY -> {
                getTodayTimeStamp() - 7 * getOneDayTime()
            }

            Constants.AppUsageQueryType.LAST_MONTH -> {
                getTodayTimeStamp() - 30 * getOneDayTime()
            }

            else -> {
                0
            }
        }

        //29919114 60804970

        val endTime = System.currentTimeMillis()
        for (appUsage in allAppUsage) {
            if (appUsage.user!!.email == email && appUsage.time <= endTime && appUsage.time >= startTime) {
                result.add(appUsage)
            }
        }
        result.sortByDescending {
            it.time
        }
        return if (result.isEmpty()) {
            resultEmpty()
        } else {
            resultSuccess(result)
        }
    }

    fun insertViolation(
        email: String,
        appPackage: String,
        appName: String,
        time: Long,
        action: Int,
        classId: Long
    ): BaseResponse<Boolean> {
        val usage = insertAppUsage(email, appPackage, appName, time, action).data!!
        val app = appRepository.getAppByAppPackage(appPackage)
        return if (app.isPresent) {
            saveReport(usage, classId)
            resultSuccess(true)
        } else {
            saveApp(appName, appPackage)
            saveReport(usage, classId)
            resultNeedUpload(true)
        }
    }

    private fun saveReport(appUsage: AppUsage, classId: Long) {
        val report = Report()
        report.appUsage = appUsage
        report.time = appUsage.time
        report.studentClass = classRepository.findById(classId).get()
        report.description = ""
        reportRepository.save(report)
    }

    private fun saveApp(appName: String, appPackage: String): App {
        val newApp = App()
        newApp.appName = appName
        newApp.packageName = appPackage
        return appRepository.save(newApp)
    }

    fun getViolation(classId: Long): BaseResponse<List<Report>> {
        val allAppUsage = reportRepository.findAll()
        val result = mutableListOf<Report>()

        for (appUsage in allAppUsage) {
            if (appUsage.studentClass!!.id == classId) {
                result.add(appUsage)
            }
        }
        result.sortByDescending {
            it.time
        }
        return if (result.isEmpty()) {
            resultEmpty()
        } else {
            resultSuccess(result)
        }
    }

    private fun saveAppSetting(
        userId: Long,
        appPackageName: String,
        lock: Boolean,
        limited: Long
    ): AppSetting {
        val userAppSetting = appSettingRepository.searchUserAppSettingsByPackage(userId, appPackageName)
        return if (userAppSetting.isNotEmpty()) {
            val result = userAppSetting[0]
            result.isLock = lock
            result.isLimited = limited
            appSettingRepository.save(result)
        } else {
            val result = AppSetting()
            val user = userRepository.findById(userId).get()
            val app = appRepository.getAppByAppPackage(appPackageName).get()
            result.user = user
            result.app = app
            result.isLock = lock
            result.isLimited = limited
            appSettingRepository.save(result)
        }
    }

    fun uploadUserAppSettings(userId: Long, appSettings: List<AppSetting>): BaseResponse<List<AppSetting>> {
        val result = mutableListOf<AppSetting>()
        for (app in appSettings) {
            result.add(saveAppSetting(app.user!!.id, app.app!!.packageName, app.isLock, app.isLimited))
        }
        return resultSuccess(result)
    }

    fun getAppSetting(userId: Long): BaseResponse<List<AppSetting>> {
        val result = appSettingRepository.searchUserAppSettings(userId)
        return if (result.isEmpty()) {
            resultEmpty()
        } else {
            resultSuccess(result)
        }
    }

    fun uploadUserAppSetting(userId: Long, appSettings: AppSetting): BaseResponse<AppSetting> {
        val result = saveAppSetting(userId, appSettings.app!!.packageName, appSettings.isLock, appSettings.isLimited)
        return resultSuccess(result)
    }

    fun uploadUserAppSetting(userId: Long, appPackage: String, isLock: Boolean, isLimited: Long): BaseResponse<AppSetting> {
        val result = saveAppSetting(userId, appPackage, isLock, isLimited)
        return resultSuccess(result)
    }

    fun checkForDatabaseApps(apps: List<String>): BaseResponse<List<String>> {
        val allApp = appRepository.findAll().map {
            it.packageName
        }
        val result = mutableListOf<String>()
        for (app in apps) {
            if (!allApp.contains(app)) {
                result.add(app)
            }
        }
        return if (result.isEmpty()) {
            resultEmpty()
        } else {
            resultSuccess(result)
        }
    }


}