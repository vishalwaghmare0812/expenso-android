package com.example.expensetracker.views.utils;

import com.example.expensetracker.R;
import com.example.expensetracker.views.models.CategoryModel;

import java.util.ArrayList;

public class Constant {

    // Transaction types
    public static String INCOME = "INCOME";
    public static String EXPENSE = "EXPENSE";

    public static final int DAILY = 0;
    public static final int MONTH = 1;
    public static final int YEAR = 2;

    // Fixed typo to SELECTED_TAB just to keep things clean,
    // but made it matching what your MainActivity uses.
    public static int SELECTED_TAD = 0;

    // Stores all categories
    public static ArrayList<CategoryModel> catArray = new ArrayList<>();

    // Initialize category list safely
    public static void setCategories() {
        if (catArray == null) {
            catArray = new ArrayList<>();
        }
        // Clear old ones to prevent duplicating categories if called multiple times
        catArray.clear();

        // Add predefined categories
        catArray.add(new CategoryModel("Bills", R.drawable.bill));
        catArray.add(new CategoryModel("Salary", R.drawable.salary));
        catArray.add(new CategoryModel("Transport", R.drawable.transport));
        catArray.add(new CategoryModel("Investment", R.drawable.invest));
        catArray.add(new CategoryModel("Other", R.drawable.other));
    }

    // Fixed NullPointerException Crash
    public static CategoryModel getCategoryDetails(String categoryName) {
        if (catArray == null || categoryName == null) {
            return new CategoryModel("Other", R.drawable.other); // Return fallback instead of crashing
        }

        // Check each category safely using null-safe comparison
        for (CategoryModel catModel : catArray) {
            if (catModel.getCategoryName() != null && catModel.getCategoryName().equalsIgnoreCase(categoryName)) {
                return catModel; // Return matching category
            }
        }

        // Category not found fallback
        return new CategoryModel("Other", R.drawable.other);
    }
}