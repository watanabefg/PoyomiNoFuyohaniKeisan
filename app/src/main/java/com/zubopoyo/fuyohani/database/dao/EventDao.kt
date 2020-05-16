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

    @Query("select * from event")
    fun getEvents() : LiveData<List<Event>>

}