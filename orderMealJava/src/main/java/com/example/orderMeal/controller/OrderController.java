package com.example.orderMeal.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.orderMeal.bean.Order;
import com.example.orderMeal.config.WebSocketServer;
import com.example.orderMeal.service.OrderService;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Random;

import java.util.*;

@CrossOrigin
@ResponseBody
@RestController
@RequestMapping("Order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    WebSocketServer webSocketServer;

    @GetMapping(value = "/selectPaidOrder")
    public List<Order> selectPaidOrder(){
        return orderService.selectPaidOrder();
    }

    @PostMapping(value = "/selectPaidOrderByUid")
    public List<Map<String, Object>> selectPaidOrderByUid(@RequestParam(value = "userId") String userId){
        return orderService.selectPaidOrderByUid(userId);
    }

    @PostMapping(value = "/selectUnpaidOrderByUid")
    public List<Order> selectUnpaidOrderByUid(@RequestParam(value = "userId") String userId){
        return orderService.selectUnpaidOrderByUid(userId);
    }

    @PostMapping(value = "/insertOrder")
    public boolean insertPaidOrder(@RequestBody List<Map<String, Object>> orderList){
        boolean status = true;
        Date date = new Date();
        String mealName, userId, ref;
        mealName = userId = ref = null;
        int mealId, quantity;
        mealId = quantity = -1;
        double price = 0;
        for(Map<String, Object> order : orderList) {
            mealId = Integer.valueOf(order.get("mealId").toString());
            mealName = order.get("mealName").toString();
            quantity = Integer.valueOf(order.get("quantity").toString());
            price = Double.valueOf(order.get("price").toString());
            userId = order.get("userId").toString();
            Order meal = orderService.selectByUidAndMid(userId, mealId);
            if(meal == null) {
                status = orderService.insertOrder(mealId, mealName, userId, quantity, price, date, false, ref);
            }else {
                quantity = meal.getQuantity() + quantity;
                status = orderService.updateQuantity(mealId, userId, quantity, date);
            }
            if(status == false) {
                break;
            }
        }
        return status;
    }

//    @PostMapping(value = "/updateQuantity")
//    public boolean updateQuantity(@RequestBody Map<String,Object> map){
//        int mealId = Integer.valueOf(map.get("mealId").toString());
//        String userId = map.get("userId").toString();
//        int quantity = Integer.valueOf(map.get("quantity").toString());
//        return orderService.updateQuantity(mealId, userId, quantity);
//    }

    @PostMapping(value = "/delUnpaidOrderByMid")
    public boolean delUnpaidOrderByMid(@RequestParam(value = "userId") String userId, @RequestParam(value = "mealId") int mealId){
        return orderService.delUnpaidOrderByMid(userId, mealId);
    }

    @PostMapping(value = "/Settlement")
    public boolean Settlement(@RequestParam(value = "userId") String userId, @RequestParam(value = "mealIdList[]") int[] mealIdList){
        boolean flag = true;
        String ref = randomStr(16);
        for(int i = 0; i < mealIdList.length; i++) {
            try {
                orderService.updatePaid(userId, mealIdList[i], ref);
                webSocketServer.onMessage("支付成功");
            }catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }

    @PostMapping(value = "/orderGroupByRef")
    public List<Map<String, Object>> orderGroupByRef(
            @RequestParam(value = "currentPage", required = false, defaultValue = "-1") int currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "-1") int pageSize,
            @RequestParam(value = "userId", required = false, defaultValue = "") String userId) {
        List<Map<String, Object>> orderList = orderService.selectGroupByRef(userId, null, null, null, null, null, currentPage, pageSize);
        for (Map<String, Object> order : orderList) {
            order.put("meal", orderService.selectByRef(order.get("ref").toString()));
        }
        return orderList;
    }

    //管理员添加订单
    @PostMapping(value = "/addOrder")
    public boolean addOrder(@RequestBody JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("orderList");
        List<Order> orderList = (List)JSONArray.parseArray(jsonArray.toJSONString(), Order.class);
        Date date = new Date();
        String ref = randomStr(16);
        for(Order order : orderList) {
            if(!orderService.insertOrder(order.getMealId(),
                    order.getMealName(),
                    order.getUserId(),
                    order.getQuantity(),
                    order.getPrice(),
                    date, true,
                    ref)) {
                return false;
            };
        }
        return true;
    }

    //查询订单
    @PostMapping(value = "/screenOrder")
    public List<Map<String, Object>> screenOrder(@RequestBody Map<String, Object> screenData) {
        String userId, sdate, edate, minPrices, maxPrices, ref;
        userId = sdate = edate = minPrices = maxPrices = ref = null;
        int currentPage = Integer.valueOf(screenData.get("currentPage").toString());
        int pageSize = Integer.valueOf(screenData.get("pageSize").toString());
        if(notNull(screenData.get("userId"))) {
            userId = screenData.get("userId").toString();
        }
        if(notNull(screenData.get("date"))) {
            ArrayList<String> date = (ArrayList<String>)screenData.get("date");
            sdate = date.get(0);
            edate = date.get(1);
        }
        if(notNull(screenData.get("ref"))) {
            ref = screenData.get("ref").toString();
        }
        if(notNull(screenData.get("minPrices"))) {
            minPrices = screenData.get("minPrices").toString();
        }
        if(notNull(screenData.get("maxPrices"))) {
            maxPrices = screenData.get("maxPrices").toString();
        }
        List<Map<String, Object>> orderList = orderService.selectGroupByRef(userId, sdate, edate, ref, minPrices, maxPrices, currentPage, pageSize);
        for (Map<String, Object> order : orderList) {
            order.put("meal", orderService.selectByRef(order.get("ref").toString()));
        }
        return orderList;
    }

    //生成16位随机字符串
    public static String randomStr(int length) {
        Random random = new Random();
        StringBuffer valSb = new StringBuffer();
        String charStr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int charLength = charStr.length();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charLength);
            valSb.append(charStr.charAt(index));
        }
        return valSb.toString();
    }

    private boolean notNull(Object object) {
        return object == null ? false : true;
    }
}
