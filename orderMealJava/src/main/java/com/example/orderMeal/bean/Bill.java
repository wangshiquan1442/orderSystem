package com.example.orderMeal.bean;

import java.util.Date;

public class Bill {
    private int mealId;
    private String mealName;
    private String category;
    private int quantities;
    private double prices;

    public Bill(int mealId, String mealName, String category, int quantities, double prices) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.category = category;
        this.quantities = quantities;
        this.prices = prices;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantities() {
        return quantities;
    }

    public void setQuantities(int quantities) {
        this.quantities = quantities;
    }

    public double getPrices() {
        return prices;
    }

    public void setPrices(double prices) {
        this.prices = prices;
    }
}
