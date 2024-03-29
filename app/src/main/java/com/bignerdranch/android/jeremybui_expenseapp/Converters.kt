package com.bignerdranch.android.jeremybui_expenseapp

import androidx.room.TypeConverter
import java.util.Date

// needed for database Date conversions
class Converters {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}
