package com.ahona.studentmanagerpro27.data


import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahona.studentmanagerpro27.model.Student

@Database(
    entities = [Student::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao
}