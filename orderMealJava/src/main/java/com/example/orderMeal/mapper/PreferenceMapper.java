package com.example.orderMeal.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PreferenceMapper {
    List<Map<String, Object>> selectDiscount();
}
