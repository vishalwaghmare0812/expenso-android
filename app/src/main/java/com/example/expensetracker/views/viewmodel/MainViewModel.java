package com.example.expensetracker.views.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracker.views.models.TransactionModel;
import com.example.expensetracker.views.utils.Constant;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainViewModel extends AndroidViewModel {

    private Realm realm;
    private Calendar calendar;

    public MutableLiveData<RealmResults<TransactionModel>> txLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> incomeLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> expenseLiveData = new MutableLiveData<>();
    // Double check that your MainActivity reads 'amountLiveDate' exactly with an 'e'
    public MutableLiveData<Double> amountLiveDate = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setupDatabase();
    }

    public void getTX(Calendar selectedCalendar) {
        // Create a copy of the calendar object to prevent modifying your reference point globally
        this.calendar = (Calendar) selectedCalendar.clone();

        // Establish the START boundary time cleanly (00:00:00.000)
        this.calendar.set(Calendar.HOUR_OF_DAY, 0);
        this.calendar.set(Calendar.MINUTE, 0);
        this.calendar.set(Calendar.SECOND, 0);
        this.calendar.set(Calendar.MILLISECOND, 0);

        Date startDate = this.calendar.getTime();
        Date endDate;

        // CRASH FIX: Dynamically configure date intervals depending on your active UI layout tab
        if (Constant.SELECTED_TAD == Constant.DAILY) {
            this.calendar.add(Calendar.DATE, 1);
        } else if (Constant.SELECTED_TAD == Constant.MONTH) {
            this.calendar.set(Calendar.DAY_OF_MONTH, 1); // Point to start of month
            startDate = this.calendar.getTime();
            this.calendar.add(Calendar.MONTH, 1); // Roll to next month baseline
        } else if (Constant.SELECTED_TAD == Constant.YEAR) {
            this.calendar.set(Calendar.DAY_OF_YEAR, 1); // Point to start of year
            startDate = this.calendar.getTime();
            this.calendar.add(Calendar.YEAR, 1); // Roll to next year baseline
        }
        endDate = this.calendar.getTime();

        // Safe Realm Fetch executions
        RealmResults<TransactionModel> tranArray = realm.where(TransactionModel.class)
                .greaterThanOrEqualTo("date", startDate)
                .lessThan("date", endDate)
                .findAll();
        txLiveData.setValue(tranArray);

        double totalIncome = realm.where(TransactionModel.class)
                .greaterThanOrEqualTo("date", startDate)
                .lessThan("date", endDate)
                .equalTo("type", Constant.INCOME)
                .sum("amount")
                .doubleValue();

        double totalExpense = realm.where(TransactionModel.class)
                .greaterThanOrEqualTo("date", startDate)
                .lessThan("date", endDate)
                .equalTo("type", Constant.EXPENSE)
                .sum("amount")
                .doubleValue();

        double totalAmount = realm.where(TransactionModel.class)
                .greaterThanOrEqualTo("date", startDate)
                .lessThan("date", endDate)
                .sum("amount")
                .doubleValue();

        incomeLiveData.setValue(totalIncome);
        expenseLiveData.setValue(totalExpense);
        amountLiveDate.setValue(totalAmount);
    }

    public void addTX(TransactionModel transactionModel) {
        if (realm != null) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(transactionModel);
            realm.commitTransaction();
        }
    }

    public void deleteTX(TransactionModel transactionModel) {
        if (realm != null) {
            realm.beginTransaction();
            transactionModel.deleteFromRealm();
            realm.commitTransaction();

            // CRASH FIX: Ensure calendar state evaluation check passes gracefully
            if (calendar == null) {
                calendar = Calendar.getInstance();
            }
            getTX(calendar);
        }
    }

    public void setupDatabase() {
        if (realm == null || realm.isClosed()) {
            realm = Realm.getDefaultInstance();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (realm != null && !realm.isClosed()) {
            realm.close(); // Clean up connections safely to prevent leak crashes
        }
    }
}