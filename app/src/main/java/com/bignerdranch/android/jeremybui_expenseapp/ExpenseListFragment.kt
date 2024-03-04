package com.bignerdranch.android.jeremybui_expenseapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.jeremybui_expenseapp.databinding.FragmentExpensesListBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    // main screen list, set up recycler, option to filter by date/category
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupCategoryFilter()
        setupDateFilter()

        binding.btnAddExpense.setOnClickListener {
            val action = ExpensesListFragmentDirections.actionExpensesListFragmentToAddEditExpenseFragment(0) // 0 for new expense
            findNavController().navigate(action)
        }

        binding.btnCategorized.setOnClickListener {
            findNavController().navigate(R.id.action_expensesListFragment_to_expenseCategoryFragment)
        }

        binding.btnReset.setOnClickListener {
            resetFilters()
        }

    }

    // call the recycler for all expense entries
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

    // filter recycler for only expenses under category
    private fun setupCategoryFilter() {
        val categories = resources.getStringArray(R.array.expense_categories_all)
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        binding.spinnerCategoryFilter.adapter = spinnerAdapter

        binding.spinnerCategoryFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                if (selectedCategory == "All") {
                    viewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
                        (binding.expensesRecyclerView.adapter as ExpensesAdapter).submitList(expenses)
                    }
                } else {
                    viewModel.getExpensesByCategory(selectedCategory).observe(viewLifecycleOwner) { filteredExpenses ->
                        (binding.expensesRecyclerView.adapter as ExpensesAdapter).submitList(filteredExpenses)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
=            }
        }
    }
    // filter expenses based on date
    private fun setupDateFilter() {
        binding.btnFilter.setOnClickListener {
            val dateString = binding.etDateFilter.text.toString()
            if (dateString.isNotEmpty()) {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                try {
                    val parsedDate: Date = dateFormat.parse(dateString)
                    val unixTimestamp = parsedDate.time
                    val formattedTimestamp = unixTimestamp.toString()
                    viewModel.filterExpensesByDate(formattedTimestamp).observe(viewLifecycleOwner) { filteredExpenses ->
                        (binding.expensesRecyclerView.adapter as ExpensesAdapter).submitList(filteredExpenses)
                    }
                } catch (e: Exception) {

                }
            }
        }
    }

    // reset recycler to initial with all entries
    private fun resetFilters() {
        binding.etDateFilter.text = null

        binding.spinnerCategoryFilter.setSelection(0)

        viewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
            (binding.expensesRecyclerView.adapter as ExpensesAdapter).submitList(expenses)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
