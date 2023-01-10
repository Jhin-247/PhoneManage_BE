package com.example.final_version2.controller

import com.example.final_version2.base.BaseResponse
import com.example.final_version2.model.AppSetting
import com.example.final_version2.model.AppUsage
import com.example.final_version2.model.Report
import com.example.final_version2.service.AppService
import com.example.final_version2.utils.MyStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/app")
class AppController {

    @Autowired
    private lateinit var appService: AppService

    @PostMapping("insert_new_app")
    fun insertNewApp(
        @RequestParam("app_package") appPackage: String,
        @RequestParam("app_name") appName: String,
        @RequestParam("image") appIcon: MultipartFile,
    ): BaseResponse<Boolean> {
        return appService.uploadApp(
            MyStringUtils().escapeDoubleQuotes(appName),
            MyStringUtils().escapeDoubleQuotes(appPackage),
            appIcon
        )
    }

    @PostMapping("upload_app_usage")
    fun uploadAppUsage(
        @RequestParam("email") email: String,
        @RequestParam("app_package") appPackage: String,
        @RequestParam("app_name") appName: String,
        @RequestParam("time") time: Long,
        @RequestParam("action") action: Int
    ): BaseResponse<AppUsage> {
        return appService.insertAppUsage(
            MyStringUtils().escapeDoubleQuotes(email),
            MyStringUtils().escapeDoubleQuotes(appPackage),
            MyStringUtils().escapeDoubleQuotes(appName),
            time,
            action
        )
    }

    @GetMapping("get_user_app_usage")
    fun getUserAppUsage(
        @RequestParam("email") email: String,
        @RequestParam("type_query") typeRequest: Int
    ): BaseResponse<List<AppUsage>> {
        return appService.getAppUsage(
            MyStringUtils().escapeDoubleQuotes(email),
            typeRequest
        )
    }

    @PostMapping("upload_violation")
    fun uploadViolation(
        @RequestParam("email") email: String,
        @RequestParam("app_package") appPackage: String,
        @RequestParam("app_name") appName: String,
        @RequestParam("time") time: Long,
        @RequestParam("action") action: Int,
        @RequestParam("class_id") classId: Long
    ): BaseResponse<Boolean> {
        return appService.insertViolation(
            MyStringUtils().escapeDoubleQuotes(email),
            MyStringUtils().escapeDoubleQuotes(appPackage),
            MyStringUtils().escapeDoubleQuotes(appName),
            time,
            action,
            classId
        )
    }

    @GetMapping("get_violation")
    fun getViolation(
        @RequestParam("class_id") classId: Long
    ): BaseResponse<List<Report>> {
        return appService.getViolation(classId)
    }

    @PostMapping("upload_user_settings")
    fun uploadUserSettings(
        @RequestParam("user_id") userId: Long,
        @RequestParam("app_setting") appSettings: List<AppSetting>
    ): BaseResponse<List<AppSetting>> {
        return appService.uploadUserAppSettings(userId, appSettings)
    }

    @PostMapping("upload_user_setting")
    fun uploadUserSetting(
        @RequestParam("user_id") userId: Long,
        @RequestParam("app_package") appPackageName: String,
        @RequestParam("is_lock") isLock: Boolean,
        @RequestParam("is_limited") isLimited: Long
    ): BaseResponse<AppSetting> {
        return appService.uploadUserAppSetting(userId, appPackageName, isLock, isLimited)
    }

    @GetMapping("get_user_setting")
    fun getUserAppSettings(
        @RequestParam("user_id") userId: Long
    ): BaseResponse<List<AppSetting>> {
        return appService.getAppSetting(userId)
    }

    @PostMapping("upload_app_for_database_check")
    fun upAPpForDatabaseCheck(
        @RequestParam("apps") apps: List<String>
    ): BaseResponse<List<String>> {
        return appService.checkForDatabaseApps(apps)
    }

}