package com.example.orderMeal.bean;

import java.util.Date;

public class Evaluation {
    private String userId;
    private int mealId;
    private String desc;
    private int star;
    private Date edate;

    public Evaluation() {}

    public Evaluation(String userId, int mealId, String desc, int star, Date edate) {
        this.userId = userId;
        this.mealId = mealId;
        this.desc = desc;
        this.star = star;
        this.edate = edate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public Date getEdate() {
        return edate;
    }

    public void setEdate(Date edate) {
        this.edate = edate;
    }
}
