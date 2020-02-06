package com.example.orderMeal;

import com.example.orderMeal.service.DiningTableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DiningTableTest {
    @Autowired
    private DiningTableService diningTableService;

    @Test
    public void selectAllDTable() {
        diningTableService.selectAllDTable();
    }
}
