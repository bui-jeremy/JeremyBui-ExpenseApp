package com.bignerdranch.android.jeremybui_expenseapp

import androidx.lifecycle.LiveData

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()

    suspend fun insert(expense: Expense) {
        expenseDao.insert(expense)
    }

    suspend fun update(expense: Expense) {
        expenseDao.update(expense)
    }
    fun getExpensesByCategory(category: String): LiveData<List<Expense>> {
        return expenseDao.getExpensesByCategory(category)
    }

    fun getExpensesByDate(date: String): LiveData<List<Expense>> {
        return expenseDao.getExpensesByDate(date)
    }

    fun getExpenseById(id: Int): LiveData<Expense> {
        return expenseDao.getExpenseById(id)
    }


}
