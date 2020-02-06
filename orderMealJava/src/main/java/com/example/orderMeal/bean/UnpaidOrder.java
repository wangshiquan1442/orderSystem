package com.example.orderMeal.bean;

public class UnpaidOrder {
    private int id;
    private int mealId;
    private int quantity;
    private String uid;

    public UnpaidOrder(int id, int mealId, int quantity, String uid) {
        this.id = id;
        this.mealId = mealId;
        this.quantity = quantity;
        this.uid = uid;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
