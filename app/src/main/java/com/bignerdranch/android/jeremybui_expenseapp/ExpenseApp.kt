package com.bignerdranch.android.jeremybui_expenseapp

import android.app.Application

class ExpenseApp : Application() {
    val repository: ExpenseRepository by lazy {
        val expenseDao = AppDatabase.getDatabase(this).expenseDao()
        ExpenseRepository(expenseDao)
    }
}
