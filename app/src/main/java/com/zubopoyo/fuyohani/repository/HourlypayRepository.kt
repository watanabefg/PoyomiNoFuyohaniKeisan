package com.zubopoyo.fuyohani.repository

import androidx.lifecycle.LiveData
import com.zubopoyo.fuyohani.database.dao.HourlypayDao
import com.zubopoyo.fuyohani.database.entity.Hourlypay

class HourlypayRepository (private val hourlypayDao: HourlypayDao) {
    val allHourlypay : LiveData<List<Hourlypay>> = hourlypayDao.getHourlypay()

    suspend fun insert(hourlyPay: Hourlypay) {
        hourlypayDao.insert(hourlyPay)
    }

    suspend fun update(hourlyPay: Hourlypay) {
        hourlypayDao.update(hourlyPay)
    }
}