package com.example.orderMeal.service;

import com.example.orderMeal.bean.Evaluation;
import com.example.orderMeal.mapper.EvaluationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class EvaluationService {
    @Autowired
    private EvaluationMapper evaluationMapper;

    public List<Map<String, Object>> selectAllEva() {
        return evaluationMapper.selectAllEva();
    }

    public List<Evaluation> selectEvaByMealId(int mealId) {
        return evaluationMapper.selectEvaByMealId(mealId);
    }

    public List<Map<String, Object>> selectGroupByMeal() {
        return evaluationMapper.selectGroupByMeal();
    }

    public Boolean insertEva(int mealId, String userId, String desc, int star, Date edate) {
        return evaluationMapper.insertEva(mealId, userId, desc, star, edate);
    }

    public Evaluation selectUserEva(int mealId, String userId) {
        return evaluationMapper.selectUserEva(mealId, userId);
    }

    public Boolean updateEva(int mealId, String userId, String desc, int star, Date edate) {
        return evaluationMapper.updateEva(mealId, userId, desc, star, edate);
    }
}
