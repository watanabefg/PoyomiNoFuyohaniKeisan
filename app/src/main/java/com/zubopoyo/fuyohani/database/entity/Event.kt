package com.zubopoyo.fuyohani.database.entity

import androidx.room.Entity

@Entity(tableName = "event", primaryKeys = ["year", "month", "day"])
data class Event(
    val year: Int,
    val month: Int,
    val day: Int,
    val workinghours: Int,
    val fee: Int // 0: なし、1:あり
)