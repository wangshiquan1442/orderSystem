package com.example.orderMeal.controller;

import com.example.orderMeal.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("Revenue")
@CrossOrigin
@ResponseBody
public class RevenueController {
    @Autowired
    private RevenueService revenueService;

    @PostMapping(value="/screenRevenue")
    public List<Map<String, Object>> screenRevenue(
            @RequestParam(value = "odate[]") String[] odate,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "cate") String cate
    ) {
        String sdate = null, edate = null;
        if(odate.length > 1) {
            sdate = odate[0];
            edate = odate[1];
        }
        return revenueService.screenRevenue(sdate, edate, sort, cate);
    }
}
