package com.example.orderMeal;

import com.example.orderMeal.service.PreferenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PreferenceTest {
    @Autowired
    private PreferenceService preferenceService;

    @Test
    void selectDiscount() {
        preferenceService.selectDiscount();
    }
}
