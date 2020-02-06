package com.example.orderMeal.service;

import com.example.orderMeal.mapper.PreferenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PreferenceService {
    @Autowired
    private PreferenceMapper preferenceMapper;

    public List<Map<String, Object>> selectDiscount() {
        return preferenceMapper.selectDiscount();
    }
}
