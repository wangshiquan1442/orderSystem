package com.example.orderMeal;

import com.example.orderMeal.bean.Meal;
import com.example.orderMeal.service.MealService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static ch.qos.logback.core.joran.spi.ConsoleTarget.SystemOut;

@SpringBootTest
class mealTest {
    @Autowired
    MealService mealService;

    @Test
    public void testSelectAll() {
        mealService.selectAllMeal();
    }

    @Test
    public void testSelectById() {
        mealService.selectMealById(1);
    }

    @Test
    public void testSelectMealByName() {
        mealService.selectMealByName("黑");
    }

    @Test
    public void testGetMealList() {
        List<Meal> mealList = new LinkedList<Meal>();
        String idList = "21,12,24,";
        String id = "";
//        char[] arr = idList.toCharArray();
        int x = 0;
        for(int i = 0; i < idList.length(); i++) {
            if(idList.charAt(i) == ',') {
                for(int j = x; j < i; j++) {
                    id += String.valueOf(idList.charAt(j));
                }
                mealList.add(mealService.selectMealById(Integer.valueOf(id)));
                id = "";
                x = i + 1;
            }
        }
        System.out.println(mealList);
    }
}
