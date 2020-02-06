package com.example.orderMeal.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Order {
    private int id;
    private int mealId;
    private String mealName;
    private String userId;
    private int quantity;
    private double price;
    private Date odate;
    private boolean paid;
    private String ref;

    public Order(int id, int mealId, String mealName, String userId, int quantity, double price, Date odate, boolean paid, String ref) {
        this.id = id;
        this.mealId = mealId;
        this.mealName = mealName;
        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
        this.odate = odate;
        this.paid = paid;
        this.ref = ref;
    }

    public Order() {

    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getOdate() {
        return odate;
    }

    public void setOdate(Date odate) {
        this.odate = odate;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
