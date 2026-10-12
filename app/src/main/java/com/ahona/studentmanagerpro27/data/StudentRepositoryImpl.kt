package com.ahona.studentmanagerpro27.data

import com.ahona.studentmanagerpro27.model.Student
import kotlinx.coroutines.flow.Flow

class StudentRepositoryImpl(
    private val studentDao: StudentDao
) : StudentRepository {

    override fun getAllStudents(): Flow<List<Student>> {
        return studentDao.getAllStudents()
    }

    override suspend fun insertStudent(student: Student) {
        studentDao.insertStudent(student)
    }


    override suspend fun updateStudent(student: Student) {
        studentDao.updateStudent(student)
    }

    override suspend fun deleteStudent(student: Student) {
        studentDao.deleteStudent(student)
    }

}