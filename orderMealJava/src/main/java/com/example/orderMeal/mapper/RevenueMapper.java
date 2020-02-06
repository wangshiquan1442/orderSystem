package com.example.orderMeal.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RevenueMapper {
    List<Map<String, Object>> screenRevenue(String sdate, String edate, String sort, String cate);
}
