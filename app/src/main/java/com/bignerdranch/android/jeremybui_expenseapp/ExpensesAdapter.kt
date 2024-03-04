// ExpensesAdapter.kt
package com.bignerdranch.android.jeremybui_expenseapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.jeremybui_expenseapp.databinding.ItemExpenseBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ExpensesAdapter(private val onItemClicked: (Expense) -> Unit) :
    ListAdapter<Expense, ExpensesAdapter.ExpenseViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val currentExpense = getItem(position)
        holder.bind(currentExpense)
    }

    // view for recycler
    class ExpenseViewHolder(private val binding: ItemExpenseBinding, private val onItemClicked: (Expense) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(expense: Expense) {
            binding.apply {
                expenseDate.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(expense.date)
                expenseAmount.text = String.format("$%.2f", expense.amount)
                expenseCategory.text = expense.category
                root.setOnClickListener {
                    onItemClicked(expense)
                }
            }
        }
    }

    // used for when we try to click on item view
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Expense>() {
            override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean = oldItem == newItem
        }
    }
}
