package com.example.registration.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val school: String,
    val province: String,
    val faculty: String,
    val department: String,
    val gender: String
)