package com.example.final_version2.controller

import com.example.final_version2.base.BaseResponse
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
    ): BaseResponse<Boolean> {
        return appService.insertAppUsage(
            MyStringUtils().escapeDoubleQuotes(email),
            MyStringUtils().escapeDoubleQuotes(appPackage),
            MyStringUtils().escapeDoubleQuotes(appName),
            time,
            action
        )
    }

//    @PostMapping("insert_new_apps")
//    fun insertNewApps(
//        @RequestParam("app_package") appPackage: String,
//        @RequestParam("app_name") appName: String,
//        @RequestParam("image") appIcon: MultipartFile,
//    ): BaseResponse<Boolean> {
//        return appService.uploadApp(appName, appPackage, appIcon)
//    }

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

}