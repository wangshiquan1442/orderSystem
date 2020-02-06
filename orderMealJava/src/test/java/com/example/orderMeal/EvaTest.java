package com.example.orderMeal;

import com.example.orderMeal.bean.Evaluation;
import com.example.orderMeal.service.EvaluationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class EvaTest {
    @Autowired
    EvaluationService evaluationService;

    @Test
    public void selectEvaByMealIdTest() {
        List<Evaluation> evaList = evaluationService.selectEvaByMealId(1);
    }

    @Test
    public void insertEvaTest() {
        Date date = new Date();
        evaluationService.insertEva(2, "1", "太肥了", 3, date);
    }
}
