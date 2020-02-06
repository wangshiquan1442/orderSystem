package com.example.orderMeal.service;

import com.example.orderMeal.bean.Bill;
import com.example.orderMeal.mapper.BillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BillService {
    @Autowired
    private BillMapper billMapper;

    public List<Bill> selectBillGroupByMealId() {
        return billMapper.selectBillGroupByMealId();
    }

    public List<Map<String, Object>> screenBillByMealName(String[] specific, String sdate, String edate, String sort) {
        return billMapper.screenBillByMealName(specific, sdate, edate, sort);
    }

    public List<Map<String, Object>> screenBillByCategory(String[] specific, String sdate, String edate, String sort) {
        return billMapper.screenBillByCategory(specific, sdate, edate, sort);
    }
}
