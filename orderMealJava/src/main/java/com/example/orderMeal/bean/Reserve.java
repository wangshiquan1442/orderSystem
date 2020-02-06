package com.example.orderMeal.bean;

import java.util.Date;

public class Reserve {
    private int id;
    private String userId;
    private String tableId;
    private Date bookDate;
    private int peopleNum;

    public Reserve(int id, String userId, String tableId, Date bookDate, int peopleNum) {
        this.id = id;
        this.userId = userId;
        this.tableId = tableId;
        this.bookDate = bookDate;
        this.peopleNum = peopleNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public Date getBookDate() {
        return bookDate;
    }

    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }
}
