package com.example.orderMeal.service;

import com.example.orderMeal.bean.Order;
import com.example.orderMeal.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    @Autowired
    OrderMapper orderMapper;

    public List<Order> selectPaidOrder() {
        return orderMapper.selectPaidOrder();
    }

    public List<Map<String, Object>> selectPaidOrderByUid(String userId) {
        return orderMapper.selectPaidOrderByUid(userId);
    }

    public List<Order> selectUnpaidOrderByUid(String userId) {
        return orderMapper.selectUnpaidOrderByUid(userId);
    }

    public Order selectByUidAndMid(String userId, int mealId) {
        return orderMapper.selectByUidAndMid(userId, mealId);
    }

    public Boolean insertOrder(int mealId, String mealName, String userId, int quantity, double price, Date odate, boolean paid, String ref) {
        return orderMapper.insertOrder(mealId, mealName, userId, quantity, price, odate, paid, ref);
    }

    public Boolean updateQuantity(int mealId, String userId, int quantity, Date odate) {
        return orderMapper.updateQuantity(mealId, userId, quantity, odate);
    }

    public Boolean delUnpaidOrderByMid(String userId, int mealId) {
        return orderMapper.delUnpaidOrderByMid(userId, mealId);
    }

    public boolean updatePaid(String userId, int mealId, String ref) {
        return orderMapper.updatePaid(userId, mealId, ref);
    }

    public List<Map<String, Object>> selectGroupByRef(String userId, String sdate, String edate, String ref, String minPrices, String maxPrices, int currentPage, int pageSize) {
        return orderMapper.selectGroupByRef(userId, sdate, edate, ref, minPrices, maxPrices, currentPage, pageSize);
    }

    public List<Map<String, Object>> selectByRef(String ref) {
        return orderMapper.selectByRef(ref);
    }
}
