package com.example.orderMeal.mapper;

import com.example.orderMeal.bean.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    //where paid = 1 order by odate, userId
    List<Order> selectPaidOrder();

    List<Map<String, Object>> selectPaidOrderByUid(String userId);

    List<Order> selectUnpaidOrderByUid(String userId);

    Boolean updateQuantity(int mealId, String userId, int quantity, Date odate);

    Order selectByUidAndMid(String userId, int mealId);

    Boolean insertOrder(int mealId, String mealName, String userId, int quantity, double price, Date odate, boolean paid, String ref);

    Boolean delUnpaidOrderByMid(String userId, int mealId);

    boolean updatePaid(String userId, int mealId, String ref);

    List<Map<String, Object>> selectGroupByRef(String userId, String sdate, String edate, String ref, String minPrices, String maxPrices, int currentPage, int pageSize);

    List<Map<String, Object>> selectByRef(String ref);
}
