package com.zubopoyo.fuyohani.repository

import androidx.lifecycle.LiveData
import com.zubopoyo.fuyohani.database.dao.EventDao
import com.zubopoyo.fuyohani.database.entity.Event

class EventRepository (private val eventDao: EventDao) {
    val allEvents : LiveData<List<Event>> = eventDao.getEvents()

    suspend fun insert(event: Event) {
        eventDao.insert(event)
    }

    suspend fun update(event:Event) {
        eventDao.update(event)
    }

    suspend fun delete(event: Event) {
        eventDao.delete(event)
    }

    suspend fun deleteMonth(year: Int, month:Int) {
        eventDao.deleteMonth(year, month)
    }
}