package com.example.orderMeal.controller;

import com.example.orderMeal.bean.Bill;
import com.example.orderMeal.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("Bill")
public class BillController {
    @Autowired
    BillService billService;

    @CrossOrigin
    @ResponseBody
    @GetMapping(value = "/selectBillGroupByMealId")
    public List<Bill> selectBillGroupByMealId() {
        return billService.selectBillGroupByMealId();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping(value = "/screenBill")
    public List<Map<String, Object>> screenBill(
            @RequestParam(value = "specific[]") String[] specific,
            @RequestParam(value = "odate[]") String[] odate,
            @RequestParam(value = "range") String range,
            @RequestParam(value = "sort") String sort) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String sdate = null, edate = null;
        if(specific.length < 1) {
            specific = null;
        }else if(specific.length == 1 && specific[0].equals("null")) {
            specific = null;
        }
        if(odate.length > 1) {
            sdate = odate[0];
            edate = odate[1];
        }
        if(range.equals("category")) {
            list = billService.screenBillByCategory(specific, sdate, edate, sort);
        } else {
            list = billService.screenBillByMealName(specific, sdate, edate, sort);
        }
        return list;
    }
}
