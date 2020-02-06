package com.example.orderMeal.controller;

import com.example.orderMeal.bean.PaidOrder;
import com.example.orderMeal.bean.RespBean;
import com.example.orderMeal.service.PaidOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("PaidOrder")
public class PaidOrderController {
    @Autowired
    PaidOrderService paidOrderService;

    @CrossOrigin
    @ResponseBody
    @PostMapping(value = "/selectPaidOrderByUid")
    public List<PaidOrder> selectAllByUid(@RequestParam(value = "uid") String uid){
        return paidOrderService.selectPaidOrderByUid(uid);
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping(value = "/insertPaidOrder")
    public RespBean insertPaidOrder(@RequestParam(value = "mealList") String mealList,
                                @RequestParam(value = "uid") String uid,
                                @RequestParam(value = "odate") Date odate,
                                @RequestParam(value = "priceList") String priceList){
        if (paidOrderService.insertPaidOrder(mealList, uid, odate, priceList)) {
            return RespBean.ok("success");
        }else {
            return RespBean.error("fail");
        }
    }
}
