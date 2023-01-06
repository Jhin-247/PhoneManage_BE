package com.example.final_version2.service

import com.example.final_version2.base.BaseResponse
import com.example.final_version2.base.resultEmpty
import com.example.final_version2.base.resultError
import com.example.final_version2.base.resultSuccess
import com.example.final_version2.model.ItemClass
import com.example.final_version2.model.StudentClass
import com.example.final_version2.model.User
import com.example.final_version2.repository.ChildRepository
import com.example.final_version2.repository.ClassRepository
import com.example.final_version2.repository.StudentClassRepository
import com.example.final_version2.repository.TeacherRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

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
        print("------------------------------\n${myClass.isPresent}\n----------------------")
        if (!myClass.isPresent) {
            return resultEmpty()
        }
        val allStudent = studentClassRepository.findAll()
        print("------------------------------\n${allStudent.size}\n----------------------")
        if (allStudent.isEmpty()) {
            return resultEmpty()
        }
        val result = mutableListOf<User>()
        for (studentClass in allStudent) {
            print("------------------------------\n${studentClass.joinClass!!.classId} and $classId\n----------------------")
            if (studentClass.joinClass!!.id == classId) {
                result.add(studentClass.child!!)
            }
        }
        return resultSuccess(result)
    }

    fun getStudentClasses(studentEmail: String): BaseResponse<List<ItemClass>> {
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
                result.add(studentClass.joinClass!!)
            }
        }
        return resultSuccess(result)
    }

    fun searchForClass(searchText: String, email: String): BaseResponse<List<ItemClass>> {
        val allClass = classRepository.findAll().distinctBy {
            it.classId
        }
        val studentClasses = getStudentClasses(email)
        val result = mutableListOf<ItemClass>()
        for (classItem in allClass) {
            if (studentClasses.data == null || studentClasses.data!!.isEmpty() || !studentClasses.data!!.contains(classItem)) {
                val newClass = classItem
                if (newClass.classId.toString().contains(searchText, true) ||
                    newClass.classname.contains(searchText, true) ||
                    newClass.teacher!!.email.contains(searchText, true) ||
                    newClass.subject.contains(searchText, true) ||
                    newClass.teacher!!.username.contains(searchText, true)
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

    fun joinClass(classId: Long, studentEmail: String): BaseResponse<Any> {
        val studentClass = StudentClass()
        studentClass.joinClass = classRepository.findById(classId).get()
        studentClass.child = childRepository.searchKidByEmail(studentEmail).get()
        studentClass.isActive = true
        studentClass.isBanned = false
        val result = studentClassRepository.save(studentClass)
        return resultSuccess(result)
    }
}