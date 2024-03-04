// ExpensesListFragment.kt
package com.bignerdranch.android.jeremybui_expenseapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.jeremybui_expenseapp.databinding.FragmentExpensesListBinding

class ExpensesListFragment : Fragment() {

    private var _binding: FragmentExpensesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((activity?.application as ExpenseApp).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExpensesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        binding.btnAddExpense.setOnClickListener {
            val action = ExpensesListFragmentDirections.actionExpensesListFragmentToAddEditExpenseFragment(0) // 0 for new expense
            findNavController().navigate(action)
        }
    }

    // setup the recyclerview for the list of expenses
    private fun setupRecyclerView() {
        val adapter = ExpensesAdapter { expense ->
            val action = ExpensesListFragmentDirections.actionExpensesListFragmentToAddEditExpenseFragment(expense.id)
            findNavController().navigate(action)
        }
        binding.expensesRecyclerView.adapter = adapter
        binding.expensesRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
            adapter.submitList(expenses)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
