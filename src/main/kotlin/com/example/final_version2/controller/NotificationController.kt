package com.example.final_version2.controller

import com.example.final_version2.base.BaseResponse
import com.example.final_version2.model.Notification
import com.example.final_version2.service.NotificationService
import com.example.final_version2.utils.MyStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/notification")
class NotificationController {

    @Autowired
    private lateinit var notificationService: NotificationService

    @GetMapping("get_user_notifications")
    fun getUserNotification(
        @RequestParam("user_email") email: String
    ): BaseResponse<List<Notification>> {
        return notificationService.getUserNotifications(
            MyStringUtils().escapeDoubleQuotes(email)
        )
    }

    @PostMapping("seen_notification")
    fun seenNotification(
        @RequestParam("notification_id") notificationId: Long
    ): BaseResponse<Notification> {
        return notificationService.seenNotification(notificationId)
    }

}