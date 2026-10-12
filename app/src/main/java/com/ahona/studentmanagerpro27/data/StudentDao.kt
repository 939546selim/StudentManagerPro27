package com.ahona.studentmanagerpro27.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ahona.studentmanagerpro27.model.Student
import kotlinx.coroutines.flow.Flow
import androidx.room.Delete

@Dao
interface  StudentDao {

    @Insert
    suspend fun insertStudent(student: Student)

    @Update
    suspend fun updateStudent(student: Student)

    @Query("SELECT * FROM students ORDER BY id DESC")
    fun getAllStudents(): Flow<List<Student>>

    @Delete
    suspend fun deleteStudent(student: Student)
}