package com.zubopoyo.fuyohani.ui.input

import android.app.Application
import androidx.lifecycle.*
import com.zubopoyo.fuyohani.database.AppDatabase
import com.zubopoyo.fuyohani.database.entity.Salary
import com.zubopoyo.fuyohani.repository.SalaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InputViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SalaryRepository

    val allSalaries: LiveData<List<Salary>>

    init {
        val salaryDao = AppDatabase.getDatabase(application, viewModelScope).salaryDao()
        repository = SalaryRepository(salaryDao)
        allSalaries = repository.allSalaries
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(salary: Salary) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(salary)
    }

    fun update(salary: Salary) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(salary)
    }
}