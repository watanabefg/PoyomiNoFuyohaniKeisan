package com.zubopoyo.fuyohani.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zubopoyo.fuyohani.database.entity.Config

@Dao
interface ConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(config: Config)

    @Update
    fun update(config: Config)

    @Query("SELECT * from configs where applicableYear = :thisYear")
    fun getConfig(thisYear: Int): LiveData<Config>
}