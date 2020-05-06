package com.zubopoyo.fuyohani.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zubopoyo.fuyohani.database.entity.Salary

@Dao
interface SalaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(salary: Salary)

    @Update
    suspend fun update(salary: Salary)

    @Delete
    suspend fun delete(salary: Salary)

    @Query("Delete from salary")
    suspend fun deleteAll()

    @Query("SELECT * from salary order by year, month")
    fun getSalaries(): LiveData<List<Salary>>

    @Query("SELECT * from salary WHERE year = :thisYear and month = :thisMonth")
    suspend fun getSalary(thisYear: Int, thisMonth: Int): Salary?

}