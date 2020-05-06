package com.zubopoyo.fuyohani.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zubopoyo.fuyohani.database.dao.ConfigDao
import com.zubopoyo.fuyohani.database.dao.SalaryDao
import com.zubopoyo.fuyohani.database.entity.Config
import com.zubopoyo.fuyohani.database.entity.Salary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Database(entities = [Config::class, Salary::class], version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigDao
    abstract fun salaryDao(): SalaryDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("")
            }
        }

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
                    populateDatabase(database.salaryDao())
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
            val year = SimpleDateFormat("yyyy").format(Date()).toInt()
            val month = SimpleDateFormat("MM").format(Date()).toInt()

            // 今月分の作成
            val thisSalary = salaryDao.getSalary(year, month)
            if (thisSalary == null){
                val salary = Salary(0, year, month, 0, 0, 0, "")
                salaryDao.insert(salary)
            }
        }
    }
}