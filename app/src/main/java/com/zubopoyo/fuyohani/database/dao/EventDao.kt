package com.zubopoyo.fuyohani.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zubopoyo.fuyohani.database.entity.Event

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event)

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("delete from event")
    suspend fun deleteAll()

    @Query("delete from event where year = :thisYear and month = :thisMonth")
    suspend fun deleteMonth(thisYear: Int, thisMonth: Int)

    @Query("select * from event")
    fun getEvents() : LiveData<List<Event>>

}