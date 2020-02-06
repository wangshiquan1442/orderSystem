package com.example.orderMeal.controller;

import com.example.orderMeal.bean.Reserve;
import com.example.orderMeal.service.ReserveService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@ResponseBody
@RestController
@RequestMapping("Reserve")
public class ReserveController {
    @Autowired
    ReserveService reserveService;
}
