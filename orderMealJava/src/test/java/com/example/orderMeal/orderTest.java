package com.example.orderMeal;

import com.example.orderMeal.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class orderTest {
    @Autowired
    OrderService orderService;

    @Test
    void insertTest() {
        Date date= new Date();
        orderService.insertOrder(1, "黑椒肥牛", "1", 1, 17.8, date, false, "");
    }
}
