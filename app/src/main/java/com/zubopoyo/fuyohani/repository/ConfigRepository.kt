package com.zubopoyo.fuyohani.repository

import androidx.lifecycle.LiveData
import com.zubopoyo.fuyohani.database.dao.ConfigDao
import com.zubopoyo.fuyohani.database.entity.Config
import java.text.SimpleDateFormat
import java.util.*

class ConfigRepository(private val configDao: ConfigDao) {
    val thisConfig: LiveData<Config> = configDao.getConfig(SimpleDateFormat("yyyy").format(Date()).toInt())

    suspend fun insert(config: Config) {
        configDao.insert(config)
    }

    suspend fun update(config: Config) {
        configDao.update(config)
    }
}