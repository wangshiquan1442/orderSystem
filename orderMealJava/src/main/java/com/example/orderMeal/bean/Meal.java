package com.example.orderMeal.bean;

public class Meal {
    private Integer id;
    private String name;
    private String desc;
    private String category;
    private float price;

    public Meal(Integer id, String name, String desc, String category, float price) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.category = category;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
