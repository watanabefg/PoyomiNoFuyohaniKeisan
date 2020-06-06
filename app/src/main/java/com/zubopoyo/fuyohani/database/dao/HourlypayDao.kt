package com.zubopoyo.fuyohani.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zubopoyo.fuyohani.database.entity.Hourlypay

@Dao
interface HourlypayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hourlypay: Hourlypay)

    @Update
    suspend fun update(hourlypay: Hourlypay)

    @Query("select * from hourlypay")
    fun getHourlypay() : LiveData<List<Hourlypay>>
}
