package com.zubopoyo.fuyohani.ui.roster

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zubopoyo.fuyohani.database.AppDatabase
import com.zubopoyo.fuyohani.database.entity.Event
import com.zubopoyo.fuyohani.database.entity.Hourlypay
import com.zubopoyo.fuyohani.repository.EventRepository
import com.zubopoyo.fuyohani.repository.HourlypayRepository
import kotlinx.coroutines.launch

class RosterViewModel (application: Application) : AndroidViewModel(application) {
    private val repository: EventRepository
    private val hourlypayRepository: HourlypayRepository
    val allEvents: LiveData<List<Event>>
    val allHourlypay: LiveData<List<Hourlypay>>

    init {
        val eventDao = AppDatabase.getDatabase(application, viewModelScope).eventDao()
        repository = EventRepository(eventDao)
        allEvents = repository.allEvents

        val hourlypayDao = AppDatabase.getDatabase(application, viewModelScope).hourlypayDao()
        hourlypayRepository = HourlypayRepository(hourlypayDao)
        allHourlypay = hourlypayRepository.allHourlypay
    }

    fun insertEvent(event: Event) {
        viewModelScope.launch {
            repository.insert(event)
        }
    }

    fun deleteMonth(year: Int, month:Int) {
        viewModelScope.launch {
            repository.deleteMonth(year, month)
        }
    }

    fun insertHourlypay(hourlypay: Hourlypay) {
        viewModelScope.launch {
            hourlypayRepository.insert(hourlypay)
        }
    }
}