package com.zubopoyo.fuyohani.ui.roster

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zubopoyo.fuyohani.database.AppDatabase
import com.zubopoyo.fuyohani.database.entity.Event
import com.zubopoyo.fuyohani.repository.EventRepository
import kotlinx.coroutines.launch

class RosterViewModel (application: Application) : AndroidViewModel(application) {
    private val repository: EventRepository
    val allEvents: LiveData<List<Event>>

    init {
        val eventDao = AppDatabase.getDatabase(application, viewModelScope).eventDao()
        repository = EventRepository(eventDao)
        allEvents = repository.allEvents
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
}