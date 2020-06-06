package com.zubopoyo.fuyohani.database.entity

import androidx.room.Entity

@Entity(tableName = "hourlypay", primaryKeys = ["year", "month"])
data class Hourlypay(
    val year: Int,
    val month: Int,
    val hourlypay: Int,
    val transportation: Int
)