package com.zubopoyo.fuyohani.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.zubopoyo.fuyohani.database.AppDatabase
import com.zubopoyo.fuyohani.database.entity.Config
import com.zubopoyo.fuyohani.database.entity.Salary
import com.zubopoyo.fuyohani.repository.ConfigRepository
import com.zubopoyo.fuyohani.repository.SalaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SalaryRepository
    val allSalaries: LiveData<List<Salary>>
    private val configRepository: ConfigRepository
    val config: LiveData<Config>


    init {
        val salaryDao = AppDatabase.getDatabase(application, viewModelScope).salaryDao()
        val configDao = AppDatabase.getDatabase(application, viewModelScope).configDao()
        repository = SalaryRepository(salaryDao)
        allSalaries = repository.allSalaries
        configRepository = ConfigRepository(configDao)
        config = configRepository.thisConfig
    }

}