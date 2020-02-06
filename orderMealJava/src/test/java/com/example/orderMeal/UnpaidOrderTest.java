package com.example.orderMeal;

import com.example.orderMeal.service.UnpaidOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UnpaidOrderTest {
    @Autowired
    UnpaidOrderService unpaidOrderService;

    @Test
    public void selectByUidTest() {
        unpaidOrderService.selectUnpaidOrderByUid("1");
    }
}
