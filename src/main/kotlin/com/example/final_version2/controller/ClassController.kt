package com.example.final_version2.controller

import com.example.final_version2.base.BaseResponse
import com.example.final_version2.model.ItemClass
import com.example.final_version2.model.JoinClassRequest
import com.example.final_version2.model.User
import com.example.final_version2.service.ClassService
import com.example.final_version2.utils.MyStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/class")
class ClassController {

    @Autowired
    private lateinit var classService: ClassService

    @PostMapping("create_class")
    fun createNewClass(
        @RequestParam("classname") classname: String,
        @RequestParam("description") description: String,
        @RequestParam("subject") subject: String,
        @RequestParam("grade") grade: Int,
        @RequestParam("teacher") teacherEmail: String
    ): BaseResponse<ItemClass> {
        val actualClassName = MyStringUtils().escapeDoubleQuotes(classname)
        val actualDescription = MyStringUtils().escapeDoubleQuotes(description)
        val actualSubject = MyStringUtils().escapeDoubleQuotes(subject)
        val actualTeacherEmail = MyStringUtils().escapeDoubleQuotes(teacherEmail)

        return classService.createClass(
            actualClassName,
            actualDescription,
            actualSubject,
            grade,
            actualTeacherEmail
        )

    }

    @GetMapping("get_classes")
    fun getClasses(
        @RequestParam("teacher") teacher: String
    ): BaseResponse<List<ItemClass>> {
        val teacherEmail = MyStringUtils().escapeDoubleQuotes(teacher)
        return classService.getClasses(teacherEmail)
    }

    @GetMapping("get_student_classes")
    fun getStudentClasses(
        @RequestParam("student_email") studentEmail: String
    ): BaseResponse<List<ItemClass>> {
        return classService.getStudentClasses(
            MyStringUtils().escapeDoubleQuotes(studentEmail)
        )
    }

    @GetMapping("get_student_in_class")
    fun getStudentInClass(
        @RequestParam("class_id") classId: Long
    ): BaseResponse<List<User>> {
        return classService.getStudentInClass(classId)
    }

    @GetMapping("search_class")
    fun searchClass(
        @RequestParam("search") search: String,
        @RequestParam("user_email") userEmail: String
    ): BaseResponse<List<ItemClass>> {
        val searchText = MyStringUtils().escapeDoubleQuotes(search)
        val email = MyStringUtils().escapeDoubleQuotes(userEmail)
        return classService.searchForClass(searchText, email)
    }

    @PostMapping("join_class")
    fun requestJoinClass(
        @RequestParam("class_id") classId: Long,
        @RequestParam("student_email") userEmail: String
    ): BaseResponse<Any> {
        val studentEmail = MyStringUtils().escapeDoubleQuotes(userEmail)
        return classService.requestJoinClass(
            classId,
            studentEmail
        )
    }

    @PostMapping("accept_join_class")
    fun acceptJoinClass(
        @RequestParam("request_id") requestId: Long
    ): BaseResponse<List<JoinClassRequest>> {
        return classService.acceptJoinClass(
            requestId
        )
    }

    @PostMapping("deny_join_class")
    fun denyJoinClass(
        @RequestParam("request_id") requestId: Long
    ): BaseResponse<List<JoinClassRequest>> {
        return classService.denyJoinClass(
            requestId
        )
    }

    @PostMapping("remove_from_class")
    fun removeFromClass(
        @RequestParam("student_id") studentId: Long,
        @RequestParam("class_id") classId: Long
    ): BaseResponse<List<User>> {
        return classService.removeFromClass(
            studentId,
            classId
        )
    }

    @PostMapping("banned_from_class")
    fun banFromClass(
        @RequestParam("student_id") studentId: Long,
        @RequestParam("class_id") classId: Long
    ): BaseResponse<List<User>> {
        return classService.banFromClass(
            studentId,
            classId
        )
    }

    @GetMapping("get_class_request")
    fun getClassRequest(
        @RequestParam("class_id") classId: Long
    ): BaseResponse<List<JoinClassRequest>> {
        return classService.getClassRequest(classId)
    }


}