package com.zubopoyo.fuyohani.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "configs")
data class Config(
    @PrimaryKey val applicableYear: Int,
    val cappedAmount: Int
)