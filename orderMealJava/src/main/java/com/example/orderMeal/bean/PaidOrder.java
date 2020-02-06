package com.example.orderMeal.bean;

import java.util.Date;

public class PaidOrder {
    private int id;
    private String mealList;
    private String uid;
    private Date odate;
    private String priceList;

    public PaidOrder(int id, String mealList, String uid, Date odate, String priceList) {
        this.id = id;
        this.mealList = mealList;
        this.uid = uid;
        this.odate = odate;
        this.priceList = priceList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMealList() {
        return mealList;
    }

    public void setMealList(String mealList) {
        this.mealList = mealList;
    }

    public String getUserId() {
        return uid;
    }

    public void setUserId(String uid) {
        this.uid = uid;
    }

    public Date getDate() {
        return odate;
    }

    public void setDate(Date odate) {
        this.odate = odate;
    }

    public String getPriceList() {
        return priceList;
    }

    public void setPriceList(String priceList) {
        this.priceList = priceList;
    }
}
