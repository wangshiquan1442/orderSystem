package com.example.orderMeal.bean;

public class DiningTable {
    private int id;
    private String tableId;
    private String status;
    private int capacity;
    private String note;

    public DiningTable(int id, String tableId, String status, int capacity, String note) {
        this.id = id;
        this.tableId = tableId;
        this.status = status;
        this.capacity = capacity;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
