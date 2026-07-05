package com.example.expensetracker.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.databinding.CategoryLayoutBinding;
import com.example.expensetracker.views.models.CategoryModel;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public interface CategoryClickListener {
        void onCategoryClicked(CategoryModel categoryModel);
    }

    private final Context context;
    private final ArrayList<CategoryModel> categoryList;
    private final CategoryClickListener categoryClickListener;

    public CategoryAdapter(Context context,
                           ArrayList<CategoryModel> categoryList,
                           CategoryClickListener categoryClickListener) {
        this.context = context;
        this.categoryList = categoryList;
        this.categoryClickListener = categoryClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.category_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        CategoryModel category = categoryList.get(position);

        holder.binding.categoryTxtView.setText(category.getCategoryName());
        holder.binding.categoryImgView.setImageResource(category.getCategoryImg());

        holder.itemView.setOnClickListener(v ->
                categoryClickListener.onCategoryClicked(category));
    }

    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {

        final CategoryLayoutBinding binding;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CategoryLayoutBinding.bind(itemView);
        }
    }
}