package com.bignerdranch.android.jeremybui_expenseapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.jeremybui_expenseapp.databinding.FragmentExpenseCategoryBinding

class ExpenseCategoryFragment : Fragment() {

    private var _binding: FragmentExpenseCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((activity?.application as ExpenseApp).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentExpenseCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    // update list of category groupings for expenses
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CategorizedExpensesAdapter()
        binding.categorizedExpensesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.categorizedExpensesRecyclerView.adapter = adapter

        viewModel.expensesGroupedByCategory.observe(viewLifecycleOwner) { categorizedExpenses ->
            adapter.submitList(categorizedExpenses)
        }
        val btnBack: Button = view.findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
