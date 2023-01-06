package com.example.final_version2.service

import com.example.final_version2.base.*
import com.example.final_version2.common.Constants
import com.example.final_version2.model.App
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
    ): BaseResponse<Boolean> {
        val user = userRepository.searchUserByEmail(email).get()
        val app = appRepository.getAppByAppPackage(appPackage)
        if (app.isPresent) {
            val result = AppUsage()
            result.app = app.get()
            result.action = action
            result.time = time
            result.user = user
            appUsageRepository.save(result)
            return resultSuccess(true)
        } else {
            val newApp = App()
            newApp.appName = appName
            newApp.packageName = appPackage
            val savedApp = appRepository.save(newApp)
            val result = AppUsage()
            result.app = savedApp
            result.action = action
            result.time = time
            result.user = user
            appUsageRepository.save(result)
            return resultNeedUpload(true)
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
        val user = userRepository.searchUserByEmail(email).get()
        val app = appRepository.getAppByAppPackage(appPackage)
        if (app.isPresent) {
            val result = AppUsage()
            result.app = app.get()
            result.action = action
            result.time = time
            result.user = user
            val savedResult = appUsageRepository.save(result)
            val report = Report()
            report.appUsage = savedResult
            report.time = savedResult.time
            report.studentClass = classRepository.findById(classId).get()
            report.description = ""
            reportRepository.save(report)
            return resultSuccess(true)
        } else {
            val newApp = App()
            newApp.appName = appName
            newApp.packageName = appPackage
            val savedApp = appRepository.save(newApp)
            val result = AppUsage()
            result.app = savedApp
            result.action = action
            result.time = time
            result.user = user
            val savedResult = appUsageRepository.save(result)
            val report = Report()
            report.appUsage = savedResult
            report.time = savedResult.time
            report.studentClass = classRepository.findById(classId).get()
            report.description = ""
            reportRepository.save(report)
            return resultNeedUpload(true)
        }
    }

    fun getViolation(classId: Long): BaseResponse<List<Report>> {
        val allAppUsage = reportRepository.findAll()
        val result = mutableListOf<Report>()

        for (appUsage in allAppUsage) {
            if (appUsage.studentClass!!.id == classId) {
                result.add(appUsage)
            }
        }
        return if (result.isEmpty()) {
            resultEmpty()
        } else {
            resultSuccess(result)
        }
    }

//    private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
//        val formattedDateList = ArrayList<String>()
//
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY, 0)
//        calendar.set(Calendar.MINUTE, 0)
//        calendar.set(Calendar.SECOND, 0)
//        for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
//            val currentTime = calendar.time
//            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
//            formattedDateList.add(dateFormat.format(currentTime))
//            calendar.add(Calendar.DAY_OF_YEAR, 1)
//        }
//        val date = calendar.time
//        date.time
//        print("------${date.time}")
//
//        return formattedDateList
//    }

}