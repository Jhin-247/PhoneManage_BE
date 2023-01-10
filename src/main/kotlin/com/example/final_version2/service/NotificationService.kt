package com.example.final_version2.service

import com.example.final_version2.base.BaseResponse
import com.example.final_version2.base.resultEmpty
import com.example.final_version2.base.resultExisted
import com.example.final_version2.base.resultSuccess
import com.example.final_version2.common.Constants
import com.example.final_version2.model.*
import com.example.final_version2.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NotificationService {

    @Autowired
    private lateinit var notificationRepository: NotificationRepository

    @Autowired
    private lateinit var addParentNotificationRepository: AddParentNotificationRepository

    @Autowired
    private lateinit var addKidNotificationRepository: AddKidNotificationRepository

    @Autowired
    private lateinit var parentRepository: ParentRepository

    @Autowired
    private lateinit var kidRepository: ChildRepository

    fun getUserNotifications(email: String): BaseResponse<List<Notification>> {
        val result = mutableListOf<Notification>()
        val allNotifications = notificationRepository.findAll()

        for (notification in allNotifications) {
            if (notification.receiver?.email == email) {
                result.add(notification)
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

//    fun createUserRequestNotification(
//        request: UserRelationshipRequest,
//        receiverEmail: String,
//        time: Long,
//        content: String
//    ): BaseResponse<RequestUserNotification> {
////        val sender = userRepository.searchUserByEmail(senderId)
//        val receiver = userRepository.searchUserByEmail(receiverEmail)
//        val lastUserRequestNotification = userRequestNotificationRepository.getLastNotificationRequest(request)
//        if (lastUserRequestNotification.isPresent) {
//            return resultExisted()
//        }
//        val notification = RequestUserNotification()
//        notification.notificationType = Constants.NotificationType.REQUEST_PARTNER
//        notification.userRelationshipRequest = request
//        notification.receiver = receiver.get()
//        notification.time = time
//        notification.content = content
//        val result = notificationRepository.save(notification)
//        return resultSuccess(result)
//    }

    fun createAddParentRequestNotification(
        request: AddParentRequest,
        receiverEmail: String,
        time: Long,
        content: String
    ): BaseResponse<AddParentNotification> {
        val receiver = parentRepository.searchParentByEmail(receiverEmail)
        val lastUserRequestNotification =
            addParentNotificationRepository.getLastAddParentRequest(receiver.get(), request)
        if (lastUserRequestNotification.isPresent) {
            return resultExisted()
        }
        val notification = AddParentNotification()
        notification.notificationType = Constants.NotificationType.REQUEST_PARTNER
        notification.request = request
        notification.receiver = receiver.get()
        notification.time = time
        notification.content = content
        val result = notificationRepository.save(notification)
        return resultSuccess(result)
    }

    fun createAddKidRequestNotification(
        request: AddKidRequest,
        receiverEmail: String,
        time: Long,
        content: String
    ): BaseResponse<AddKidNotification> {
        val receiver = kidRepository.searchKidByEmail(receiverEmail)
        val lastUserRequestNotification =
            addKidNotificationRepository.getLastAddKidRequest(receiver.get(), request)
        if (lastUserRequestNotification.isPresent) {
            return resultExisted()
        }
        val notification = AddKidNotification()
        notification.notificationType = Constants.NotificationType.REQUEST_KID
        notification.request = request
        notification.receiver = receiver.get()
        notification.time = time
        notification.content = content
        val result = notificationRepository.save(notification)
        return resultSuccess(result)
    }

    fun seenNotification(notificationId: Long): BaseResponse<Notification> {
        var notification = notificationRepository.findById(notificationId).get()
        notification.isRead = true
        notification = notificationRepository.save(notification)
        return resultSuccess(notification)
    }

}