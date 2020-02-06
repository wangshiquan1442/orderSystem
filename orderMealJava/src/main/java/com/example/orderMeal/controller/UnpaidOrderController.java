package com.example.orderMeal.controller;

import com.example.orderMeal.bean.UnpaidOrder;
import com.example.orderMeal.service.UnpaidOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("UnpaidOrder")
public class UnpaidOrderController {
    @Autowired
    UnpaidOrderService unpaidOrderService;

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/selectUnpaidOrderByUid",method = RequestMethod.POST)
    public List<UnpaidOrder> selectAllByUid(@RequestParam(value = "uid") String uid){
        return unpaidOrderService.selectUnpaidOrderByUid(uid);
    }
}
