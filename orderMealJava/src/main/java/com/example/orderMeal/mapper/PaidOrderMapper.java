package com.example.orderMeal.mapper;

import com.example.orderMeal.bean.PaidOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface PaidOrderMapper {
    List<PaidOrder> selectPaidOrderByUid(String uid);
    Boolean insertPaidOrder(String mealList, String uid, Date odate, String priceList);
}
