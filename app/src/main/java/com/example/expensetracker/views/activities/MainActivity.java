package com.example.expensetracker.views.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensetracker.R;
import com.example.expensetracker.databinding.ActivityMainBinding;
import com.example.expensetracker.views.adapters.TransactionAdapter;
import com.example.expensetracker.views.fregmants.AddTransactionFragment;
import com.example.expensetracker.views.utils.Constant;
import com.example.expensetracker.views.utils.Helper;
import com.example.expensetracker.views.viewmodel.MainViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Calendar calendar;
    public MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constant.setCategories();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Expense Tracker");
        }

        binding.floatingActionButton.setOnClickListener(view ->
                new AddTransactionFragment().show(getSupportFragmentManager(), "AddTransaction"));

        calendar = Calendar.getInstance();
        binding.tranList.setLayoutManager(new LinearLayoutManager(this));

        // Observers
        viewModel.txLiveData.observe(this, txLiveData -> {
            TransactionAdapter tranAdapter = new TransactionAdapter(MainActivity.this, txLiveData);
            binding.tranList.setAdapter(tranAdapter);

            if (txLiveData != null && txLiveData.size() > 0) {
                binding.emptyState.setVisibility(View.GONE);
            } else {
                binding.emptyState.setVisibility(View.VISIBLE);
            }
        });

        viewModel.incomeLiveData.observe(this, aDouble ->
                binding.incomeLbl.setText(String.valueOf(aDouble)));

        viewModel.expenseLiveData.observe(this, aDouble ->
                binding.expensesLbl.setText(String.valueOf(aDouble)));

        viewModel.amountLiveDate.observe(this, aDouble ->
                binding.totalAmountLbl.setText(String.valueOf(aDouble)));

        // Run baseline setup
        updateDate();

        // FIXED: Match converted ViewBinding fields accurately
        binding.nextDateBtn.setOnClickListener(view -> {
            if (Constant.SELECTED_TAD == 0) {
                calendar.add(Calendar.DATE, 1);
            } else if (Constant.SELECTED_TAD == 1) {
                calendar.add(Calendar.MONTH, 1);
            } else if (Constant.SELECTED_TAD == 2) {
                calendar.add(Calendar.YEAR, 1);
            }
            updateDate();
        });

        binding.previousDateBtn.setOnClickListener(view -> {
            if (Constant.SELECTED_TAD == 0) {
                calendar.add(Calendar.DATE, -1);
            } else if (Constant.SELECTED_TAD == 1) {
                calendar.add(Calendar.MONTH, -1);
            } else if (Constant.SELECTED_TAD == 2) {
                calendar.add(Calendar.YEAR, -1);
            }
            updateDate();
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText() != null) {
                    String tabText = tab.getText().toString();
                    if ("Daily".equals(tabText)) {
                        Constant.SELECTED_TAD = 0;
                    } else if ("Month".equals(tabText)) {
                        Constant.SELECTED_TAD = 1;
                    } else if ("Year".equals(tabText)) {
                        Constant.SELECTED_TAD = 2;
                    }
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    public void getTransactions() {
        viewModel.getTX(calendar);
    }

    private void updateDate() {
        if (Constant.SELECTED_TAD == Constant.DAILY) {
            binding.currentDateTv.setText(Helper.formatDate(calendar.getTime()));
        } else if (Constant.SELECTED_TAD == Constant.MONTH) {
            binding.currentDateTv.setText(Helper.formatDateByMonth(calendar.getTime()));
        } else if (Constant.SELECTED_TAD == Constant.YEAR) {
            binding.currentDateTv.setText(Helper.formatDateByYear(calendar.getTime()));
        }
        viewModel.getTX(calendar);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.top_menu, menu);
//        return true;
//    }
}