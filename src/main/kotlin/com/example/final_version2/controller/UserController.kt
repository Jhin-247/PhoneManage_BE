package com.example.final_version2.controller

import com.example.final_version2.base.BaseResponse
import com.example.final_version2.base.resultError
import com.example.final_version2.base.resultSuccess
import com.example.final_version2.common.Constants
import com.example.final_version2.model.*
import com.example.final_version2.service.NotificationService
import com.example.final_version2.service.UserService
import com.example.final_version2.utils.MyStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var notificationService: NotificationService

    @PostMapping("create_account")
    fun createSuperAccount(
        @RequestParam("email") email: String,
        @RequestParam("password") password: String,
        @RequestParam("username") username: String,
        @RequestParam("role") role: Int
    ): BaseResponse<User> {
        val emailToSave = MyStringUtils().escapeDoubleQuotes(email)
        val passwordToSave = MyStringUtils().escapeDoubleQuotes(password)
        val usernameToSave = MyStringUtils().escapeDoubleQuotes(username)

        when (role) {
            Constants.Role.PERSONAL -> {
                return userService.createNormalUser(
                    emailToSave,
                    passwordToSave,
                    usernameToSave,
                    role
                )
            }

            Constants.Role.PARENT -> {
                return userService.createParentUser(
                    emailToSave,
                    passwordToSave,
                    usernameToSave,
                    role
                )
            }

            Constants.Role.TEACHER -> {
                return userService.createTeacher(
                    emailToSave,
                    passwordToSave,
                    usernameToSave,
                    role
                )
            }
        }

        return resultError("Something when wrong")
    }

    @PostMapping("create_sub_account")
    fun createSubAccount(
        @RequestParam("super_user") superEmail: String,
        @RequestParam("email") email: String,
        @RequestParam("password") password: String,
        @RequestParam("username") username: String,
        @RequestParam("role") role: Int
    ): BaseResponse<User> {
        val superUser = MyStringUtils().escapeDoubleQuotes(superEmail)
        val emailToSave = MyStringUtils().escapeDoubleQuotes(email)
        val passwordToSave = MyStringUtils().escapeDoubleQuotes(password)
        val usernameToSave = MyStringUtils().escapeDoubleQuotes(username)


        when (role) {
            Constants.Role.CHILD -> {
                return userService.createChildUser(
                    superUser,
                    emailToSave,
                    passwordToSave,
                    usernameToSave,
                    role
                )
            }

            Constants.Role.PARENT -> {
                return userService.createParentUser(
                    emailToSave,
                    passwordToSave,
                    usernameToSave,
                    role
                )
            }
        }

        return resultError("Something when wrong")

    }

    @PostMapping("change_avatar")
    fun changeAvatar(
        @RequestParam("image") multipartFile: MultipartFile,
        @RequestParam("email") email: String,
        @RequestParam("access_token") accessToken: String
    ): BaseResponse<User> {
        val savePictureResponse = userService.changeAvatar(
            multipartFile,
            MyStringUtils().escapeDoubleQuotes(email),
            MyStringUtils().escapeDoubleQuotes(accessToken)
        )

        val user = userService.getUserByEmail(
            MyStringUtils().escapeDoubleQuotes(email)
        )
        return if (user.code == Constants.Code.SUCCESS) {
            user.data!!.avatarUrl = savePictureResponse.data!!
            user
        } else {
            user
        }
    }

    @PostMapping("sign_in")
    fun signIn(
        @RequestParam("email") email: String,
        @RequestParam("password") password: String
    ): BaseResponse<User> {
        return userService.signIn(
            MyStringUtils().escapeDoubleQuotes(email),
            MyStringUtils().escapeDoubleQuotes(password)
        )
    }

    @PostMapping("change_username")
    fun changeUsername(
        @RequestParam("email") email: String,
        @RequestParam("access_token") accessToken: String,
        @RequestParam("username") username: String
    ): BaseResponse<User> {
        val user = userService.getUserByEmail(
            MyStringUtils().escapeDoubleQuotes(email)
        )
        return when (user.code) {
            Constants.Code.SUCCESS -> {
                userService.changeUsername(email, username)
            }

            else -> {
                resultError(user.message)
            }
        }
    }

    @PostMapping("get_user_info")
    fun getUserInfo(
        @RequestParam("email") email: String,
        @RequestParam("access_token") accessToken: String
    ): BaseResponse<User> {
        return userService.getUserByEmail(
            MyStringUtils().escapeDoubleQuotes(email)
        )
    }

    @GetMapping("get_partner_info")
    fun getPartnerInfo(
        @RequestParam("email") email: String
    ): BaseResponse<Parent> {
        return userService.getPartner(
            MyStringUtils().escapeDoubleQuotes(email)
        )
    }

    @GetMapping("get_all_users")
    fun getAllUser(): BaseResponse<List<User>> {
        return userService.findAll()
    }

    @GetMapping("get_all_children")
    fun getAllChildren(
        @RequestParam("email") email: String
    ): BaseResponse<List<User>> {
        return userService.getAllChildren(
            MyStringUtils().escapeDoubleQuotes(email)
        )
    }

    @PostMapping("upload_user_usage")
    fun uploadUserUsage(
        @RequestParam("email") email: String,
        @RequestParam("app_package") appPackageName: String,
        @RequestParam("time") time: Long,
        @RequestParam("action") action: Int
    ): BaseResponse<Boolean> {
        return userService.uploadUserUsage(
            MyStringUtils().escapeDoubleQuotes(email),
            MyStringUtils().escapeDoubleQuotes(appPackageName),
            time,
            action
        )
    }

    @PostMapping("add_partner")
    fun requestAddPartner(
        @RequestParam("requester_id") requesterEmail: String,
        @RequestParam("receiver_id") receiverEmail: String,
        @RequestParam("request_time") requestTime: Long,
        @RequestParam("action") action: Int
    ): BaseResponse<AddParentRequest> {
        val user = userService.getUserByEmail(
            MyStringUtils().escapeDoubleQuotes(requesterEmail)
        )
        var content = ""
        if (action == Constants.RequestType.REQUEST_ADD_PARTNER) {
            content = "${user.data!!.username} has sent you a partner request"
        }
        val request = userService.requestAddPartner(
            MyStringUtils().escapeDoubleQuotes(requesterEmail),
            MyStringUtils().escapeDoubleQuotes(receiverEmail),
            requestTime,
            action
        ).data!!
        notificationService.createAddParentRequestNotification(
            request = request,
            MyStringUtils().escapeDoubleQuotes(receiverEmail),
            requestTime,
            content
        )
        return resultSuccess(request)
    }

    @PostMapping("add_child")
    fun requestAddChild(
        @RequestParam("requester_id") requesterEmail: String,
        @RequestParam("receiver_id") receiverEmail: String,
        @RequestParam("request_time") requestTime: Long,
        @RequestParam("action") action: Int
    ): BaseResponse<AddKidRequest> {
        val user = userService.getUserByEmail(
            MyStringUtils().escapeDoubleQuotes(requesterEmail)
        )
        var content = ""
        if (action == Constants.RequestType.REQUEST_ADD_CHILD) {
            content = "${user.data!!.username} has sent you a parent - child request"
        }
        val request = userService.requestAddChild(
            MyStringUtils().escapeDoubleQuotes(requesterEmail),
            MyStringUtils().escapeDoubleQuotes(receiverEmail),
            requestTime,
            action
        ).data!!
        notificationService.createAddKidRequestNotification(
            request = request,
            MyStringUtils().escapeDoubleQuotes(receiverEmail),
            requestTime,
            content
        )
        return resultSuccess(request)
    }

    @GetMapping("search_partner")
    fun searchPartner(
        @RequestParam("search_partner") search: String,
        @RequestParam("email") email: String
    ): BaseResponse<List<User>> {
        return userService.searchPartner(
            MyStringUtils().escapeDoubleQuotes(email),
            MyStringUtils().escapeDoubleQuotes(search)
        )
    }

    @GetMapping("search_children")
    fun searchChildren(
        @RequestParam("search_children") search: String,
        @RequestParam("email") email: String
    ): BaseResponse<List<User>> {
        return userService.searchChildren(
            MyStringUtils().escapeDoubleQuotes(email),
            MyStringUtils().escapeDoubleQuotes(search)
        )
    }

    @PostMapping("action_for_request")
    fun handleUserRequestAction(
        @RequestParam("sender") senderEmail: String,
        @RequestParam("request_id") requestId: Long,
        @RequestParam("action") action: Int
    ): BaseResponse<UserRelationshipRequest> {
        val request = userService.handleRequestAction(
            MyStringUtils().escapeDoubleQuotes(senderEmail),
            requestId,
            action
        )
        if (request.code == Constants.Code.SUCCESS) {
            val content = if (action == Constants.RequestType.ACCEPT_ADD_PARTNER) {
                "${request.data!!.receiver!!.username} has accepted your request"
            } else {
                "${request.data!!.receiver!!.username} has denied your request"
            }
            notificationService.createUserRequestNotification(
                request.data!!,
                request.data!!.requester!!.email,
                request.data!!.requestTime,
                content
            )
        }
        return request
    }

    @PostMapping("update_user_information")
    fun updateUserInformation(
        @RequestParam("sender") userEmail: String,
        @RequestParam("image") multipartFile: MultipartFile?,
        @RequestParam("username") username: String?,
        @RequestParam("password") password: String?
    ): BaseResponse<User> {
        return userService.updateUserInfo(
            MyStringUtils().escapeDoubleQuotes(userEmail),
            multipartFile,
            if (username == null) {
                null
            } else {
                MyStringUtils().escapeDoubleQuotes(username)
            },
            if (password == null) {
                null
            } else {
                MyStringUtils().escapeDoubleQuotes(password)
            }
        )
    }

}