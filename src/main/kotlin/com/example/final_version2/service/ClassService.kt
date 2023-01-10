package com.example.final_version2.service

import com.example.final_version2.base.BaseResponse
import com.example.final_version2.base.resultEmpty
import com.example.final_version2.base.resultError
import com.example.final_version2.base.resultSuccess
import com.example.final_version2.model.ItemClass
import com.example.final_version2.model.JoinClassRequest
import com.example.final_version2.model.StudentClass
import com.example.final_version2.model.User
import com.example.final_version2.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassService {

    @Autowired
    private lateinit var teacherRepository: TeacherRepository

    @Autowired
    private lateinit var classRepository: ClassRepository

    @Autowired
    private lateinit var studentClassRepository: StudentClassRepository

    @Autowired
    private lateinit var childRepository: ChildRepository

    @Autowired
    private lateinit var joinClassRequestRepository: JoinClassRequestRepository

    fun createClass(
        actualClassName: String,
        actualDescription: String,
        actualSubject: String,
        grade: Int,
        actualTeacherEmail: String
    ): BaseResponse<ItemClass> {
        val teacher = teacherRepository.searchTeacherByEmail(actualTeacherEmail)
        if (!teacher.isPresent) {
            return resultError("Cannot find teacher")
        }
        val itemClass = ItemClass()
        itemClass.teacher = teacher.get()
        itemClass.timeCreated = System.currentTimeMillis()
        itemClass.grade = grade
        itemClass.classname = actualClassName
        itemClass.description = actualDescription
        itemClass.subject = actualSubject
        val result = classRepository.save(itemClass)
        return resultSuccess(result)
    }

    fun getClasses(teacherEmail: String): BaseResponse<List<ItemClass>> {
        val teacher = teacherRepository.searchTeacherByEmail(teacherEmail)
        if (!teacher.isPresent) {
            return resultError("No teacher found")
        }
        val result = classRepository.findByTeacher(teacher.get())
        return if (result.isEmpty()) {
            resultEmpty()
        } else {
            resultSuccess(result)
        }
    }

    fun getStudentInClass(classId: Long): BaseResponse<List<User>> {
        val myClass = classRepository.findById(classId)
        if (!myClass.isPresent) {
            return resultEmpty()
        }
        val allStudent = studentClassRepository.findAll()
        if (allStudent.isEmpty()) {
            return resultEmpty()
        }
        val result = mutableListOf<User>()
        for (studentClass in allStudent) {
            if (studentClass.joinClass!!.id == classId && !studentClass.isBanned) {
                result.add(studentClass.child!!)
            }
        }
        return resultSuccess(result)
    }

    fun getStudentClasses(studentEmail: String, showIsBanned: Boolean = false): BaseResponse<List<ItemClass>> {
        val student = childRepository.searchKidByEmail(studentEmail)
        if (!student.isPresent) {
            return resultError("No student found")
        }
        val allClass = studentClassRepository.findAll()
        if (allClass.isEmpty()) {
            return resultEmpty()
        }
        val result = mutableListOf<ItemClass>()
        for (studentClass in allClass) {
            if (studentClass.child!!.email == studentEmail) {
                if (studentClass.isBanned && showIsBanned) {
                    result.add(studentClass.joinClass!!)
                } else if (!studentClass.isBanned) {
                    result.add(studentClass.joinClass!!)
                }

            }
        }
        return resultSuccess(result)
    }

    fun searchForClass(searchText: String, email: String): BaseResponse<List<ItemClass>> {
        val allClass = classRepository.findAll().distinctBy {
            it.classId
        }
        val studentClasses = getStudentClasses(email, true)
        val result = mutableListOf<ItemClass>()
        for (classItem in allClass) {
            if (studentClasses.data == null || studentClasses.data!!.isEmpty() || !studentClasses.data!!.contains(
                    classItem
                )
            ) {
                if (classItem.classId.toString().contains(searchText, true) ||
                    classItem.classname.contains(searchText, true) ||
                    classItem.teacher!!.email.contains(searchText, true) ||
                    classItem.subject.contains(searchText, true) ||
                    classItem.teacher!!.username.contains(searchText, true)
                ) {
                    val studentClass = studentClasses.data!!
                    var canAdd = true
                    for (stuClass in studentClass) {
                        if (stuClass.id == classItem.id) {
                            canAdd = false
                        }
                    }
                    if (canAdd) {
                        result.add(classItem)
                    }
                }

            }
        }
        return if (result.isEmpty()) {
            resultEmpty()
        } else {
            resultSuccess(result)
        }

    }

    fun requestJoinClass(classId: Long, studentEmail: String): BaseResponse<Any> {
        val request = JoinClassRequest()
        request.classToJoin = classRepository.findById(classId).get()
        request.requester = childRepository.searchKidByEmail(studentEmail).get()
        request.time = System.currentTimeMillis()
        return resultSuccess(joinClassRequestRepository.save(request))
    }

    fun acceptJoinClass(requestId: Long): BaseResponse<List<JoinClassRequest>> {
        val request = joinClassRequestRepository.findById(requestId).get()
        val studentClass = StudentClass()
        studentClass.joinClass = request.classToJoin
        studentClass.child = request.requester
        studentClass.isActive = true
        studentClass.isBanned = false
        studentClassRepository.save(studentClass)
        joinClassRequestRepository.deleteById(requestId)
        val result = getClassRequestList(request.classToJoin.id)
        return if (result.isEmpty()) {
            resultEmpty()
        } else {
            resultSuccess(result)
        }
    }

    fun denyJoinClass(requestId: Long): BaseResponse<List<JoinClassRequest>> {
        val request = joinClassRequestRepository.findById(requestId).get()
        joinClassRequestRepository.delete(request)
        val result = getClassRequestList(request.classToJoin.id)
        return if (result.isEmpty()) {
            resultEmpty()
        } else {
            resultSuccess(result)
        }
    }

    fun removeFromClass(studentId: Long, classId: Long): BaseResponse<List<User>> {
        val studentClass = studentClassRepository.findStudentClassByStudentAndClass(studentId, classId).get()
        studentClassRepository.delete(studentClass)
        val result = getStudentInClass(classId)
        return resultSuccess(result.data!!)
    }

    fun banFromClass(studentId: Long, classId: Long): BaseResponse<List<User>> {
        val studentClass = studentClassRepository.findStudentClassByStudentAndClass(studentId, classId).get()
        studentClass.isBanned = true
        studentClassRepository.save(studentClass)
        val result = getStudentInClass(classId)
        return resultSuccess(result.data!!)
    }

    fun getClassRequest(classId: Long): BaseResponse<List<JoinClassRequest>> {
        val result = getClassRequestList(classId)
        return if (result.isEmpty()) {
            resultEmpty()
        } else {
            resultSuccess(result)
        }
    }

    private fun getClassRequestList(classId: Long): List<JoinClassRequest> {
        return joinClassRequestRepository.findRequestByClassId(classId)
    }

}