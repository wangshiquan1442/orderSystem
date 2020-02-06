package com.example.orderMeal.mapper;

import com.example.orderMeal.bean.Bill;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BillMapper {
    List<Bill> selectBillGroupByMealId();

    List<Map<String, Object>> screenBillByMealName(String[] specific, String sdate, String edate, String sort);

    List<Map<String, Object>> screenBillByCategory(String[] specific, String sdate, String edate, String sort);
}
