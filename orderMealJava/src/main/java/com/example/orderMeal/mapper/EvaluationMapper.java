package com.example.orderMeal.mapper;

import com.example.orderMeal.bean.Evaluation;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface EvaluationMapper {
    List<Map<String, Object>> selectAllEva();
    List<Evaluation> selectEvaByMealId(int mealId);
    List<Map<String, Object>> selectGroupByMeal();
    Boolean insertEva(int mealId, String userId, String desc, int star, Date edate);
    Evaluation selectUserEva(int mealId, String userId);
    Boolean updateEva(int mealId, String userId, String desc, int star, Date edate);
}
