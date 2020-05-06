package com.zubopoyo.fuyohani.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.zubopoyo.fuyohani.database.AppDatabase
import com.zubopoyo.fuyohani.database.entity.Salary
import com.zubopoyo.fuyohani.repository.SalaryRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SalaryRepository
    val allSalaries: LiveData<List<Salary>>
    init {
        val salaryDao = AppDatabase.getDatabase(application, viewModelScope).salaryDao()
        repository = SalaryRepository(salaryDao)
        allSalaries = repository.allSalaries
    }
}