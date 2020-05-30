package com.zubopoyo.fuyohani.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zubopoyo.fuyohani.database.dao.ConfigDao
import com.zubopoyo.fuyohani.database.dao.EventDao
import com.zubopoyo.fuyohani.database.dao.SalaryDao
import com.zubopoyo.fuyohani.database.entity.Config
import com.zubopoyo.fuyohani.database.entity.Event
import com.zubopoyo.fuyohani.database.entity.Salary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Database(entities = [Config::class, Salary::class, Event::class], version = 5, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigDao
    abstract fun salaryDao(): SalaryDao
    abstract fun eventDao(): EventDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "poyomi_fuyohani_keisan"
                )
                    .addCallback((AppDatabaseCallback(scope)))
                    .fallbackToDestructiveMigrationFrom(1)
                    .fallbackToDestructiveMigrationFrom(2)
                    .fallbackToDestructiveMigrationFrom(3)
                    .fallbackToDestructiveMigrationFrom(4)
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }

        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    //populateDatabase(database.salaryDao())            // TODO: リリース前には削除する
                    initializeSalary(database.salaryDao()) // 今月分の作成
                    //populateEvent(database.eventDao())            // TODO: リリース前には削除する
                }
            }
        }

        suspend fun initializeSalary(salaryDao: SalaryDao) {
            // 今月分の作成
            val year = SimpleDateFormat("yyyy").format(Date()).toInt()
            val month = SimpleDateFormat("MM").format(Date()).toInt()

            (1..month).forEach {
                val thisSalary = salaryDao.getSalary(year, it)
                if (thisSalary == null){
                    val salary = Salary(0, year, it, 0, 0, 0, "")
                    salaryDao.insert(salary)
                }
            }
        }
        suspend fun populateDatabase(salaryDao: SalaryDao) {
            // Delete all content here
            salaryDao.deleteAll()

            // Add sample salary
            var salary = Salary(1, 2020, 3, 50000, 10000, 0, "test")
            salaryDao.insert(salary)
            salary = Salary(2, 2020, 4, 110000, 10000, 0, "")
            salaryDao.insert(salary)
            salary = Salary(3, 2019, 8, 80000, 10000, 0, "1st")
            salaryDao.insert(salary)


        }

        suspend fun populateEvent(eventDao: EventDao) {
            eventDao.deleteAll()

            // Add sample event
            var event = Event(2020, 5, 9, 4f,0)
            eventDao.insert(event)
            event = Event(2020, 5, 8, 3f,1)
            eventDao.insert(event)
        }
    }
}