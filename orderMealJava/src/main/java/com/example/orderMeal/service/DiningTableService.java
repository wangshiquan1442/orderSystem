package com.example.orderMeal.service;

import com.example.orderMeal.bean.DiningTable;
import com.example.orderMeal.mapper.DiningTableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiningTableService {
    @Autowired
    private DiningTableMapper diningTableMapper;

    public List<DiningTable> selectAllDTable() {
        return diningTableMapper.selectAllDTable();
    }

    public Boolean updateDTable(String tableId, int capacity, String status, String note) {
        return diningTableMapper.updateDTable(tableId, capacity, status, note);
    }

    public Boolean insertDTable(String tableId, int capacity, String status, String note) {
        return diningTableMapper.insertDTable(tableId, capacity, status, note);
    }
}
