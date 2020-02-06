package com.example.orderMeal.service;

import com.example.orderMeal.bean.Reserve;
import com.example.orderMeal.mapper.ReserveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReserveService {
    @Autowired
    private ReserveMapper reserveMapper;
    public List<Reserve> selectAllReserve() {
        return reserveMapper.selectAllReserve();
    }
}
