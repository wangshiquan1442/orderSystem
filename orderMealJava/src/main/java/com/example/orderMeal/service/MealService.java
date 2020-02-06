package com.example.orderMeal.service;

import com.example.orderMeal.bean.Meal;
import com.example.orderMeal.mapper.MealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MealService {
    @Autowired
    MealMapper mealMapper;

    public List<Meal> selectAllMeal(){
        return mealMapper.selectAllMeal();
    }

    public Meal selectMealById(int id) {
        return mealMapper.selectMealById(id);
    }

    public List<Meal> selectMealByName(String name){
        return mealMapper.selectMealByName(name);
    }

    public List<Map<String, Object>> selectMealCate() {
        return mealMapper.selectMealCate();
    }

    public Boolean insertMeal(String name, String desc, String category, float price) {
        return mealMapper.insertMeal(name, desc, category, price);
    }

    public Boolean updateMeal(Integer id, String name, String desc, String category, float price) {
        return mealMapper.updateMeal(id, name, desc, category, price);
    }

    public Boolean delMeal(String id) {
        return mealMapper.delMeal(id);
    }
}
