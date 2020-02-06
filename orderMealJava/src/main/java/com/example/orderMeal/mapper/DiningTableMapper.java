package com.example.orderMeal.mapper;

import com.example.orderMeal.bean.DiningTable;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiningTableMapper {
    List<DiningTable> selectAllDTable();
    Boolean updateDTable(String tableId, int capacity, String status, String note);
    Boolean insertDTable(String tableId, int capacity, String status, String note);
}
