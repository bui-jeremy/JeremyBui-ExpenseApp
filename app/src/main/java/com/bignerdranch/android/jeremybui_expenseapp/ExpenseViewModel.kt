package com.bignerdranch.android.jeremybui_expenseapp

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    val allExpenses: LiveData<List<Expense>> = repository.allExpenses

    fun insert(expense: Expense) = viewModelScope.launch {
        repository.insert(expense)
    }
    fun update(expense: Expense) = viewModelScope.launch {
        repository.update(expense)
    }
    fun delete(expense: Expense) = viewModelScope.launch {
        repository.delete(expense)
    }

    fun getExpenseById(id: Int): LiveData<Expense> {
        return repository.getExpenseById(id)
    }

}
