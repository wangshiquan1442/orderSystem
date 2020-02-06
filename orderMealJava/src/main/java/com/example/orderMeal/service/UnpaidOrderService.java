package com.example.orderMeal.service;

import com.example.orderMeal.bean.UnpaidOrder;
import com.example.orderMeal.mapper.UnpaidOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnpaidOrderService {
    @Autowired
    UnpaidOrderMapper unpaidOrderMapper;

    public List<UnpaidOrder> selectUnpaidOrderByUid(String uid) {
        return unpaidOrderMapper.selectUnPaidOrderByUid(uid);
    }
}

