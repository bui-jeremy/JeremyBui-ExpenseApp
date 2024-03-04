package com.bignerdranch.android.jeremybui_expenseapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.map
import kotlinx.coroutines.launch


class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    val allExpenses: LiveData<List<Expense>> = repository.allExpenses

    val expensesGroupedByCategory: LiveData<List<CategoryWithTotal>> = allExpenses.map { expenses ->
        expenses.groupBy { it.category }
            .map { entry -> CategoryWithTotal(category = entry.key, total = entry.value.sumOf { it.amount }) }
    }

    fun insert(expense: Expense) = viewModelScope.launch {
        repository.insert(expense)
    }
    fun update(expense: Expense) = viewModelScope.launch {
        repository.update(expense)
    }

    fun getExpensesByCategory(category: String): LiveData<List<Expense>> {
        return repository.getExpensesByCategory(category)
    }

    fun getExpenseById(id: Int): LiveData<Expense> {
        return repository.getExpenseById(id)
    }

    fun filterExpensesByDate(date: String): LiveData<List<Expense>> {
        return repository.getExpensesByDate(date)
    }
}

data class CategoryWithTotal(val category: String, val total: Double)
