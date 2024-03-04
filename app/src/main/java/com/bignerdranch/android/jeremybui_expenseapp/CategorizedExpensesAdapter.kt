package com.bignerdranch.android.jeremybui_expenseapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategorizedExpensesAdapter : RecyclerView.Adapter<CategorizedExpensesAdapter.CategoryViewHolder>() {

    private var categoriesWithTotals: List<CategoryWithTotal> = emptyList()

    // updating recycler based on categories
    fun submitList(list: List<CategoryWithTotal>) {
        categoriesWithTotals = list
        notifyDataSetChanged()
    }

    // updating view for total
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_category_with_total, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = categoriesWithTotals[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = categoriesWithTotals.size

    // update text view from category item
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(categoryWithTotal: CategoryWithTotal) {
            itemView.findViewById<TextView>(R.id.categoryName).text = categoryWithTotal.category
            itemView.findViewById<TextView>(R.id.totalExpense).text = String.format("$%.2f", categoryWithTotal.total)
        }
    }
}
