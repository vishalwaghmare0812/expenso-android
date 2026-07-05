package com.example.expensetracker.views.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TransactionModel extends RealmObject {

    private String type;
    private String category;
    private String note;
    private double amount;
    private Date date;
    @PrimaryKey
    private long id;


    public TransactionModel() {
        // Required by Realm
    }

    public TransactionModel(String type, String category, String note, double amount, Date date, long id) {
        this.type = type;
        this.category = category;
        this.note = note;
        this.amount = amount;
        this.date = date;
        this.id= id;
    }

//    public TransactionModel(String type, String category, String note, double amount, Date date) {
//        this.type = type;
//        this.category = category;
//        this.note = note;
//        this.amount = amount;
//        this.date = date;
//    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getNote() {
        return note;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public long getId() {
        return id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setId(long id) {
        this.id = id;
    }
}