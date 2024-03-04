package com.bignerdranch.android.jeremybui_expenseapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.jeremybui_expenseapp.databinding.FragmentAddEditExpenseBinding
import java.text.SimpleDateFormat
import java.util.*

class AddEditExpenseFragment : Fragment() {

    private var _binding: FragmentAddEditExpenseBinding? = null
    private val binding get() = _binding!!

    private val args: AddEditExpenseFragmentArgs by navArgs()

    private val viewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((activity?.application as ExpenseApp).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddEditExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()

        val expenseId = args.expenseId
        if (expenseId > 0) {
            viewModel.getExpenseById(expenseId).observe(viewLifecycleOwner) { expense ->
                populateUiForEdit(expense)
            }
        }

        binding.saveExpenseButton.setOnClickListener {
            if (expenseId > 0) {
                updateExpense(expenseId)
            } else {
                saveExpense()
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    // load existing data into addeditexpensefragment
    private fun populateUiForEdit(expense: Expense) {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        binding.editTextDate.setText(dateFormat.format(expense.date))
        binding.editTextAmount.setText(expense.amount.toString())
        val categories = resources.getStringArray(R.array.expense_categories)
        binding.categorySpinner.setSelection(categories.indexOf(expense.category))
    }

    // load spinner using category array
    private fun setupSpinner() {
        val categories = resources.getStringArray(R.array.expense_categories)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter
    }

    // from btnSaveExpense to save into DB
    private fun saveExpense() {
        val (amount, date, category) = getExpenseInput()
        if (amount != null && date != null) {
            val expense = Expense(date = date, amount = amount, category = category)
            viewModel.insert(expense)
            findNavController().navigateUp()
        }
    }

    // if we clicked view, then update the current existing entity
    private fun updateExpense(id: Int) {
        val (amount, date, category) = getExpenseInput()
        if (amount != null && date != null) {
            val expense = Expense(id = id, date = date, amount = amount, category = category)
            viewModel.update(expense)
            findNavController().navigateUp()
        }
    }

    // parsing the form to add/modify entry in DB
    private fun getExpenseInput(): Triple<Double?, Date?, String> {
        val amount = binding.editTextAmount.text.toString().toDoubleOrNull()
        val dateString = binding.editTextDate.text.toString()
        val category = binding.categorySpinner.selectedItem.toString()

        if (amount == null) {
            return Triple(null, null, "")
        }

        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val date = try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }

        if (date == null) {
            return Triple(null, null, "")
        }

        return Triple(amount, date, category)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
