package com.example.final_version2.service

import com.example.final_version2.base.*
import com.example.final_version2.common.Constants
import com.example.final_version2.model.*
import com.example.final_version2.repository.*
import com.example.final_version2.utils.FileUploadUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var parentRepository: ParentRepository

    @Autowired
    private lateinit var childRepository: ChildRepository

    @Autowired
    private lateinit var userRelationshipRepository: UserRelationshipRepository

    @Autowired
    private lateinit var notificationService: NotificationService

    @Autowired
    private lateinit var addParentRequestRepository: AddParentRequestRepository

    @Autowired
    private lateinit var addKidRequestRepository: AddKidRequestRepository

    @Autowired
    private lateinit var teacherRepository: TeacherRepository

    fun createNormalUser(
        email: String,
        password: String,
        username: String,
        role: Int
    ): BaseResponse<User> {
        val user = userRepository.searchUserByEmail(email)
        if (user.isPresent) {
            return resultExisted()
        }
        val userToSave = User()
        userToSave.username = username
        userToSave.role = role
        userToSave.email = email
        userToSave.password = password
        userToSave.accessToken = System.currentTimeMillis().toString()
        userToSave.uid = System.currentTimeMillis().toString()
        val result = userRepository.save(userToSave)
        return resultSuccess(result)
    }

    fun createParentUser(
        email: String,
        password: String,
        username: String,
        role: Int
    ): BaseResponse<User> {
        val user = userRepository.searchUserByEmail(email)
        if (user.isPresent) {
            return resultExisted()
        }
        val userToSave = Parent()
        userToSave.username = username
        userToSave.role = role
        userToSave.email = email
        userToSave.password = password
        userToSave.accessToken = System.currentTimeMillis().toString()
        userToSave.uid = System.currentTimeMillis().toString()
        val result = parentRepository.save(userToSave)
        return resultSuccess(result)
    }

    fun createTeacher(
        emailToSave: String,
        passwordToSave: String,
        usernameToSave: String,
        role: Int
    ): BaseResponse<User> {
        val user = userRepository.searchUserByEmail(emailToSave)
        if (user.isPresent) {
            return resultExisted()
        }
        val userToSave = Teacher()
        userToSave.username = usernameToSave
        userToSave.role = role
        userToSave.email = emailToSave
        userToSave.password = passwordToSave
        userToSave.accessToken = System.currentTimeMillis().toString()
        userToSave.uid = System.currentTimeMillis().toString()
        val result = teacherRepository.save(userToSave)
        return resultSuccess(result)
    }

    fun changeAvatar(image: MultipartFile, email: String, accessToken: String): BaseResponse<String> {
        val result = BaseResponse<String>()
        val user = userRepository.searchUserByEmail(email)
//        if (accessToken != user.get().accessToken) {
//            result.code = Constants.Code.ERROR
//            result.description = Constants.Description.ERROR
//            result.message = Constants.Message.NO_LONGER_LOGGED_IN
//            return result
//        }
        var myEmail = email
        if (myEmail.contains('@')) {
            myEmail = email.substring(0, email.indexOf('@'))
        }
        val userImageFileName = StringUtils.cleanPath("$myEmail.jpg")
        val uploadDir = "user-photos"
        FileUploadUtil.saveFile(uploadDir, userImageFileName, image)
        result.code = Constants.Code.SUCCESS
        result.description = Constants.Description.SUCCESS
        result.message = Constants.Message.SUCCESS
        result.data = email
        val newUserSave = user.get()
        newUserSave.avatarUrl = email
        userRepository.save(newUserSave)
        return result
    }

    fun getUserByEmail(email: String): BaseResponse<User> {
        val user = userRepository.searchUserByEmail(email)
        if (user.isPresent) {
            return resultSuccess(user.get())
        }
        return resultError("No user found")
    }

    fun signIn(email: String, password: String): BaseResponse<User> {
        val user = userRepository.searchUserByEmail(email)
        return if (user.isPresent) {
            val myUser = user.get()
            if (myUser.password == password) {
                myUser.accessToken = System.currentTimeMillis().toString()
                val result = userRepository.save(myUser)
                resultSuccess(result)
            } else {
                resultError("Wrong password")
            }
        } else {
            resultError("No email found")
        }
    }

    fun changeUsername(email: String, username: String): BaseResponse<User> {
        val user = userRepository.searchUserByEmail(email)
        return if (user.isPresent) {
            val myUser = user.get()
            myUser.username = username
            val result = userRepository.save(myUser)
            resultSuccess(result)
        } else {
            resultError("No email found")
        }
    }

    fun createSubUser(
        superUser: String,
        email: String,
        password: String,
        username: String,
        role: Int
    ): BaseResponse<User> {
        val user = userRepository.searchUserByEmail(email)
        if (user.isPresent) {
            return resultExisted()
        }
        val userToSave = User()
        userToSave.username = username
        userToSave.role = role
        userToSave.email = email
        userToSave.password = password
        userToSave.accessToken = "-1"
        userToSave.uid = System.currentTimeMillis().toString()
        val result = userRepository.save(userToSave)


        return resultSuccess(result)
    }

    fun createChildUser(
        superUser: String,
        emailToSave: String,
        passwordToSave: String,
        usernameToSave: String,
        role: Int
    ): BaseResponse<User> {
        val user = userRepository.searchUserByEmail(emailToSave)
        if (user.isPresent) {
            return resultExisted()
        }
        val userToSave = Child()
        userToSave.username = usernameToSave
        userToSave.role = role
        userToSave.email = emailToSave
        userToSave.password = passwordToSave
        userToSave.accessToken = "-1"
        userToSave.superUser1 = userRepository.searchUserByEmail(superUser).get()
        userToSave.uid = System.currentTimeMillis().toString()
        val result = childRepository.save(userToSave)
        return resultSuccess(result)
    }

    fun getPartner(email: String): BaseResponse<Parent> {
//        val user = getUserByEmail(email)
//        val parent = user.data!! as Parent
//        if (parent.user == null) {
//            return resultEmpty()
//        }
//        return resultSuccess(parent.user!!)

        val user = parentRepository.searchParentByEmail(email)
        return if (!user.isPresent || user.get().partnerEmail.isEmpty()) {
            resultEmpty()
        } else {
            resultSuccess(parentRepository.searchParentByEmail(user.get().partnerEmail).get())
        }
    }

    fun findAll(): BaseResponse<List<User>> {
        return resultSuccess(userRepository.findAll())
    }

    fun getAllChildren(email: String): BaseResponse<List<User>> {
        val childrenList = childRepository.findAll()
        val result = mutableListOf<Child>()
        for (child in childrenList) {
            val supervisorOne = child.superUser1
            val supervisorTwo = child.superUser2
            if ((supervisorOne != null && supervisorOne.email == email) || (supervisorTwo != null && supervisorTwo.email == email)) {
                result.add(child)
            }
        }
        return if (result.isEmpty()) {
            resultEmpty()
        } else {
            resultSuccess(result)
        }
    }

    fun uploadUserUsage(email: String, appPackageName: String, time: Long, action: Int): BaseResponse<Boolean> {
        return resultSuccess(true)
    }

    fun requestAddPartner(
        requesterEmail: String,
        receiverEmail: String,
        requestTime: Long,
        action: Int
    ): BaseResponse<AddParentRequest> {

        val requester = parentRepository.searchParentByEmail(requesterEmail)
        val receiver = parentRepository.searchParentByEmail(receiverEmail)

        var request = AddParentRequest()
        val allRequestForTwo = addParentRequestRepository.getAllRequest(requester.get().email, receiver.get().email)
        if (allRequestForTwo.isNotEmpty() && allRequestForTwo[0].action == action) {
            request = allRequestForTwo[0]
        } else {
            request.requester = requester.get()
            request.partner = receiver.get()
            request.action = action
            request.time = requestTime
            request = addParentRequestRepository.save(request)
        }
        return resultSuccess(request)
    }

    fun searchPartner(email: String, stringToSearch: String): BaseResponse<List<User>> {
//        val allUsers = userRepository.findAll()       // Switch role needed
        val allUsers = parentRepository.findAll()
        val result = mutableListOf<User>()
        for (user in allUsers) {
            if (user.role != Constants.Role.CHILD
                && user.role != Constants.Role.STUDENT
                && user.role != Constants.Role.PERSONAL
                && user.username.contains(stringToSearch, ignoreCase = true)
                && user.email != email
            ) {
                result.add(user)
            }
        }

        return if (result.isEmpty()) {
            resultEmpty()
        } else {
            resultSuccess(result)
        }

    }

    fun searchChildren(email: String, search: String): BaseResponse<List<User>> {
        val children = childRepository.findAll()
        val result = mutableListOf<Child>()
        for (child in children) {
            if ((child.superUser1 != null && child.superUser1!!.email == email) || (child.superUser2 != null && child.superUser2!!.email == email) || !child.username.contains(
                    search,
                    true
                )
            ) {

            } else {
                result.add(child)
            }
        }
        return if (result.isEmpty()) {
            resultEmpty()
        } else {
            resultSuccess(result)
        }
    }

    fun handleRequestAction(email: String, requestId: Long, action: Int): BaseResponse<UserRelationshipRequest> {
        val request = userRelationshipRepository.findById(requestId).get()
        val lastRequest = userRelationshipRepository.getLastRequest(request.requester!!, request.receiver!!).get()
        var newRequest = UserRelationshipRequest()
        if (lastRequest.action == Constants.RequestType.REQUEST_ADD_PARTNER) {
            newRequest.action = action
            newRequest.requester = request.requester
            newRequest.receiver = request.receiver
            newRequest.requestTime = System.currentTimeMillis()
            newRequest = userRelationshipRepository.save(newRequest)
        }
        return if (request.requester == null) {
            resultError("Something went wrong")
        } else {
            resultSuccess(newRequest)
        }
    }

    fun updateUserInfo(
        userEmail: String,
        multipartFile: MultipartFile? = null,
        username: String? = null,
        password: String? = null
    ): BaseResponse<User> {
        multipartFile?.let {
            changeAvatar(it, userEmail, "")
        }
        val user = userRepository.searchUserByEmail(userEmail).get()
        if (username != null)
            user.username = username
        if (password != null)
            user.password = password

        return resultSuccess(userRepository.save(user))

    }

    fun requestAddChild(
        requesterEmail: String,
        receiverEmail: String,
        requestTime: Long,
        action: Int
    ): BaseResponse<AddKidRequest> {
        val requester = parentRepository.searchParentByEmail(requesterEmail)
        val receiver = childRepository.searchKidByEmail(receiverEmail)
        var request = AddKidRequest()
        val allRequestForTwo = addKidRequestRepository.getAllRequest(requester.get().email, receiver.get().email)
        if (allRequestForTwo.isNotEmpty() && allRequestForTwo[0].action == action) {
            request = allRequestForTwo[0]
        } else {
            request.requester = requester.get()
            request.child = receiver.get()
            request.action = action
            request.time = requestTime
            request = addKidRequestRepository.save(request)
        }
        return resultSuccess(request)
    }

}