package com.zubopoyo.fuyohani.ui.settings

import android.app.Application
import androidx.lifecycle.*
import com.zubopoyo.fuyohani.database.AppDatabase
import com.zubopoyo.fuyohani.database.entity.Config
import com.zubopoyo.fuyohani.repository.ConfigRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ConfigRepository
    val config: LiveData<Config>

    init {
        val configDao = AppDatabase.getDatabase(application, viewModelScope).configDao()
        repository = ConfigRepository(configDao)
        config = repository.thisConfig
    }

    /*
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    */

    fun insert(config: Config) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(config)
    }

    fun update(config: Config) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(config)
    }

}