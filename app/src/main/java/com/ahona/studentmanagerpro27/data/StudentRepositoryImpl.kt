package com.ahona.studentmanagerpro27.data

import com.ahona.studentmanagerpro27.model.Student
import kotlinx.coroutines.flow.Flow

//class StudentRepositoryImpl(
//    private val studentDao: StudentDao
//) : StudentRepository {
//
//    override fun getAllStudents(): Flow<List<Student>> {
//        return studentDao.getAllStudents()
//    }
//
//    override suspend fun insertStudent(student: Student) {
//        studentDao.insertStudent(student)
//    }
//
//
//    override suspend fun updateStudent(student: Student) {
//        studentDao.updateStudent(student)
//    }
//
//    override suspend fun deleteStudent(student: Student) {
//        studentDao.deleteStudent(student)
//    }
//
//}




class StudentRepositoryImpl(
    private val studentDao: StudentDao
) : StudentRepository {

    override fun getAllStudents(): Flow<List<Student>> {
        return studentDao.getAllStudents()
    }

    override fun getStudentsSortedByName(): Flow<List<Student>> {
        return studentDao.getStudentsSortedByName()
    }

    override fun getStudentsByMinimumAge(minAge: Int): Flow<List<Student>> {
        return studentDao.getStudentsByMinimumAge(minAge)

    }

    override fun getStudentsByExactAge(
        age: Int
    ): Flow<List<Student>> {
        return studentDao.getStudentsByExactAge(age)
    }

    override suspend fun insertStudent(
        student: Student
    ): StudentOperationResult {
        return try {
            studentDao.insertStudent(student)
            StudentOperationResult.Success
        } catch (e: Exception) {
            StudentOperationResult.Error(
                "Unable to save student. Please try again."
            )
        }
    }

    override suspend fun updateStudent(
        student: Student
    ): StudentOperationResult {
        return try {
            studentDao.updateStudent(student)
            StudentOperationResult.Success
        } catch (e: Exception) {
            StudentOperationResult.Error(
                "Unable to update student. Please try again."
            )
        }
    }

    override suspend fun deleteStudent(
        student: Student
    ): StudentOperationResult {
        return try {
            studentDao.deleteStudent(student)
            StudentOperationResult.Success
        } catch (e: Exception) {
            StudentOperationResult.Error(
                "Unable to delete student. Please try again."
            )
        }
    }
}