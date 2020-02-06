package com.example.orderMeal.mapper;

import com.example.orderMeal.bean.Reserve;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReserveMapper {
    List<Reserve> selectAllReserve();
}
