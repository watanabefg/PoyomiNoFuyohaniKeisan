package com.zubopoyo.fuyohani.repository

import androidx.lifecycle.LiveData
import com.zubopoyo.fuyohani.database.dao.SalaryDao
import com.zubopoyo.fuyohani.database.entity.Salary

class SalaryRepository(private val salaryDao: SalaryDao) {
    val allSalaries: LiveData<List<Salary>> = salaryDao.getSalaries()

    suspend fun insert(salary: Salary) {
        salaryDao.insert(salary)
    }

    suspend fun update(salary: Salary) {
        salaryDao.update(salary)
    }
}