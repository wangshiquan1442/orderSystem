package com.example.orderMeal.service;

import com.example.orderMeal.bean.PaidOrder;
import com.example.orderMeal.mapper.PaidOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PaidOrderService {
    @Autowired
    PaidOrderMapper paidOrderMapper;

    public List<PaidOrder> selectPaidOrderByUid(String uid) {
        return paidOrderMapper.selectPaidOrderByUid(uid);
    }

    public Boolean insertPaidOrder(String mealList, String uid, Date odate, String priceList) {
        return paidOrderMapper.insertPaidOrder(mealList, uid, odate, priceList);
    }
}
