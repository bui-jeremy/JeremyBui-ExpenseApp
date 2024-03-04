package com.bignerdranch.android.jeremybui_expenseapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Expense::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class) // type converter for Date
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // retrieve database
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
               // room database building
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "expense_database"
                )
                    .fallbackToDestructiveMigration() // destroy previous if migration
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
