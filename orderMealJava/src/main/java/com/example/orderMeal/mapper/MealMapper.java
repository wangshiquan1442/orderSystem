package com.example.orderMeal.mapper;

import com.example.orderMeal.bean.Meal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MealMapper {
    List<Meal> selectAllMeal();

    List<Meal> selectMealByName(String name);

    Meal selectMealById(int id);

    List<Map<String, Object>> selectMealCate();

    Boolean insertMeal(String name, String desc, String category, float price);

    Boolean updateMeal(Integer id, String name, String desc, String category, float price);

    Boolean delMeal(String id);
}
