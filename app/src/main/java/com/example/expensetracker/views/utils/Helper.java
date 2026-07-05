package com.example.expensetracker.views.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {

    // Convert Date to readable string format (e.g., "05 July, 2026")
    public static String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    // Convert Date to month format (e.g., "July, 2026")
    public static String formatDateByMonth(Date date) {
        if (date == null) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    // Convert Date to year format (e.g., "2026")
    public static String formatDateByYear(Date date) {
        if (date == null) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }
}