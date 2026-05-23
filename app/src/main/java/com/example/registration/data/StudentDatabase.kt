package com.example.registration.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Student::class], version = 7, exportSchema = false)
abstract class StudentDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
}