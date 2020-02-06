package com.example.orderMeal.controller;

import com.example.orderMeal.bean.Evaluation;
import com.example.orderMeal.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@ResponseBody
@RestController
@RequestMapping("Evaluation")
public class EvaluationController {
    @Autowired
    EvaluationService evaluationService;

    @GetMapping(value = "/selectAllEva")
    public List<Map<String, Object>> selectAllEva() {
        return evaluationService.selectAllEva();
    }

    @PostMapping(value = "/selectEvaByMealId")
    public List<Evaluation> selectEvaByMealId(@RequestParam(value = "mealId") int mealId) {
        return evaluationService.selectEvaByMealId(mealId);
    }

    @GetMapping(value = "/selectGroupByMeal")
    public List<Map<String, Object>> selectGroupByMeal() {
        //根据菜品的汇总
        List<Map<String, Object>> evaGroup = evaluationService.selectGroupByMeal();
        //每条评论
        List<Map<String, Object>> evaList = evaluationService.selectAllEva();
        for(Map<String, Object> group : evaGroup) {
            List<Map<String, Object>> evaTemp = new ArrayList<Map<String, Object>>();
            for(Map<String, Object> map : evaList) {
                if(Integer.valueOf(group.get("mealId").toString()) == map.get("mealId")) {
                    evaTemp.add(map);
                }
            }
            group.put("evaList", evaTemp);
        }
        return evaGroup;
    }

    @PostMapping(value = "/insertEval")
    public Boolean insertEva(@RequestBody List<Map<String, Object>> evaList) {
        Date edate = new Date();
        int mealId, star;
        mealId = star = 0;
        String userId, desc;
        userId = desc = null;
        Boolean status = true;
        for(Map<String, Object> eva : evaList) {
            mealId = Integer.valueOf(eva.get("mealId").toString());
            star = Integer.valueOf(eva.get("star").toString());
            userId = eva.get("userId").toString();
            desc = eva.get("desc").toString();
            if(evaluationService.selectUserEva(mealId, userId) != null) {
                status = evaluationService.updateEva(mealId, userId, desc, star, edate);
            }else {
                status = evaluationService.insertEva(mealId, userId, desc, star, edate);
            }
            if(status == false) {
                break;
            }
        }
        return status;
    }
}
