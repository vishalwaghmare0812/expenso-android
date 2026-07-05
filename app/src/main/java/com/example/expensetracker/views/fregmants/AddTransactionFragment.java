package com.example.expensetracker.views.fregmants;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.expensetracker.R;
import com.example.expensetracker.databinding.FragmentAddTransactionBinding;
import com.example.expensetracker.databinding.ListDialogBinding;
import com.example.expensetracker.views.activities.MainActivity;
import com.example.expensetracker.views.adapters.CategoryAdapter;
import com.example.expensetracker.views.models.CategoryModel;
import com.example.expensetracker.views.models.TransactionModel;
import com.example.expensetracker.views.utils.Constant;
import com.example.expensetracker.views.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class AddTransactionFragment extends BottomSheetDialogFragment {

    private FragmentAddTransactionBinding binding;
    private TransactionModel transactionModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater, container, false);
        transactionModel = new TransactionModel();

        // Setup default system state safely
        Calendar defaultCalendar = Calendar.getInstance();
        transactionModel.setDate(defaultCalendar.getTime());
        transactionModel.setId(defaultCalendar.getTime().getTime());
        transactionModel.setType(Constant.EXPENSE);
        transactionModel.setCategory("Other"); // Safe baseline fallback

        if (getContext() != null) {
            binding.expencesBtn.setBackground(getContext().getDrawable(R.drawable.expences_selector));
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.defualt_selector));
            binding.date.setText(Helper.formatDate(defaultCalendar.getTime()));
        }

        // Switch to Income mode
        binding.incomeBtn.setOnClickListener(view -> {
            if (getContext() != null) {
                binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
                binding.expencesBtn.setBackground(getContext().getDrawable(R.drawable.defualt_selector));
            }
            transactionModel.setType(Constant.INCOME);
        });

        // Switch to Expense mode (UNCOMMENTED AND FIXED)
        binding.expencesBtn.setOnClickListener(view -> {
            if (getContext() != null) {
                binding.expencesBtn.setBackground(getContext().getDrawable(R.drawable.expences_selector));
                binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.defualt_selector));
            }
            transactionModel.setType(Constant.EXPENSE);
        });

        // Open date picker safely
        binding.date.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
            datePickerDialog.setOnDateSetListener((datePicker, year, month, dayOfMonth) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);

                binding.date.setText(Helper.formatDate(calendar.getTime()));
                transactionModel.setDate(calendar.getTime());
                transactionModel.setId(calendar.getTime().getTime());
            });
            datePickerDialog.show();
        });

        // Open category selection dialog safely
        binding.category.setOnClickListener(view -> {
            ListDialogBinding listBinding = ListDialogBinding.inflate(inflater);
            AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
            alertDialog.setView(listBinding.getRoot());

            CategoryAdapter categoryAdapter = new CategoryAdapter(
                    getContext(),
                    Constant.catArray,
                    category -> {
                        binding.category.setText(category.getCategoryName());
                        transactionModel.setCategory(category.getCategoryName());
                        alertDialog.dismiss();
                    });

            listBinding.recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 3));
            listBinding.recyclerview.setAdapter(categoryAdapter);
            alertDialog.show();
        });

        // CRASH FIX: Validation logic added on save
        binding.btnSaveTransaction.setOnClickListener(view -> {
            String amountStr = binding.amount.getText().toString().trim();
            String note = binding.note.getText().toString().trim();

            if (amountStr.isEmpty()) {
                binding.amount.setError("Please enter an amount");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                binding.amount.setError("Invalid amount format");
                return;
            }

            if (transactionModel.getType().equals(Constant.EXPENSE)) {
                transactionModel.setAmount(amount * -1);
            } else {
                transactionModel.setAmount(amount);
            }

            transactionModel.setNote(note);

            // Context-safe checks for parent Activity communication
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity.viewModel != null) {
                    mainActivity.viewModel.addTX(transactionModel);
                    mainActivity.getTransactions();
                }
            }
            dismiss();
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}