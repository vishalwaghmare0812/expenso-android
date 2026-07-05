package com.example.expensetracker.views.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.databinding.RowTransactionBinding;
import com.example.expensetracker.views.activities.MainActivity;
import com.example.expensetracker.views.models.CategoryModel;
import com.example.expensetracker.views.models.TransactionModel;
import com.example.expensetracker.views.utils.Constant;

import io.realm.RealmResults;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private final Context context;
    private final RealmResults<TransactionModel> tranArray;

    public TransactionAdapter(Context context, RealmResults<TransactionModel> tranArray) {
        this.context = context;
        this.tranArray = tranArray;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionModel tranModel = tranArray.get(position);

        if (tranModel == null) return;

        // Display transaction details safely
        holder.binding.tranAmountTxv.setText(String.valueOf(tranModel.getAmount()));
        holder.binding.tranCatTxv.setText(tranModel.getCategory() != null ? tranModel.getCategory() : "Other");
        holder.binding.tranNoteTxv.setText(tranModel.getNote() != null ? tranModel.getNote() : "");

        // CRASH FIX: Guard category details loop
        CategoryModel catModel = Constant.getCategoryDetails(tranModel.getCategory());
        if (catModel != null) {
            holder.binding.tranImg.setImageResource(catModel.getCategoryImg());
        } else {
            holder.binding.tranImg.setImageResource(R.drawable.other); // Baseline fallback
        }

        // CRASH FIX: Safe color extraction helper to handle missing resource color targets
        int greenColor, redColor;
        try {
            greenColor = ContextCompat.getColor(context, R.color.darkGreen);
        } catch (Exception e) {
            greenColor = android.graphics.Color.GREEN; // System safety fallback
        }

        try {
            redColor = ContextCompat.getColor(context, R.color.darkRed);
        } catch (Exception e) {
            redColor = android.graphics.Color.RED; // System safety fallback
        }

        if (Constant.INCOME.equals(tranModel.getType())) {
            holder.binding.tranAmountTxv.setTextColor(greenColor);
        } else {
            holder.binding.tranAmountTxv.setTextColor(redColor);
        }

        holder.itemView.setOnLongClickListener(v -> {
            AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
            deleteDialog.setTitle("Delete Dialog");
            deleteDialog.setMessage("Are you sure you want to delete this transaction?");

            deleteDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
                if (context instanceof MainActivity) {
                    ((MainActivity) context).viewModel.deleteTX(tranModel);
                }
            });
            deleteDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialogInterface, i) -> deleteDialog.dismiss());
            deleteDialog.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return tranArray != null ? tranArray.size() : 0;
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        RowTransactionBinding binding;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}