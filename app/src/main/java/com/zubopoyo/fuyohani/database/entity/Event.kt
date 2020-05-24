package com.zubopoyo.fuyohani.database.entity

import androidx.room.Entity

@Entity(tableName = "event", primaryKeys = ["year", "month", "day"])
data class Event(
    val year: Int,
    val month: Int,
    val day: Int,
    val workinghours: Float,
    val fee: Int // 0: なし、1:あり
)