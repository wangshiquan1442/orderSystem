package com.example.orderMeal.service;

import com.example.orderMeal.mapper.RevenueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RevenueService {
    @Autowired
    private RevenueMapper revenueMapper;

    public List<Map<String, Object>> screenRevenue(String sdate, String edate, String sort, String cate) {
        return revenueMapper.screenRevenue(sdate, edate, sort, cate);
    }
}
