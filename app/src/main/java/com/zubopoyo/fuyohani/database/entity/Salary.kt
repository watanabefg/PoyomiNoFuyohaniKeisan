package com.zubopoyo.fuyohani.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "salary", indices = [Index(value = ["year", "month"], unique = true)])
data class Salary(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val year: Int,
    val month: Int,
    val salary: Int,
    val expenses: Int,
    val extraordinaryIncome: Int,
    val memo: String
)