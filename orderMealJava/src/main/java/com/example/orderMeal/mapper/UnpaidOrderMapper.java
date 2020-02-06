package com.example.orderMeal.mapper;

import com.example.orderMeal.bean.UnpaidOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UnpaidOrderMapper {
    List<UnpaidOrder> selectUnPaidOrderByUid(String uid);
}
