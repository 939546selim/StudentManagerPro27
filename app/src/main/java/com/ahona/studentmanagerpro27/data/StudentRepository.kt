package com.ahona.studentmanagerpro27.data

import com.ahona.studentmanagerpro27.model.Student
import kotlinx.coroutines.flow.Flow

interface StudentRepository {

    fun getAllStudents(): Flow<List<Student>>

    suspend fun insertStudent(student: Student)


    suspend fun updateStudent(student: Student)

    suspend fun deleteStudent(student: Student)

}