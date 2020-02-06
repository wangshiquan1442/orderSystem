package com.example.orderMeal.controller;

import com.example.orderMeal.bean.DiningTable;
import com.example.orderMeal.bean.Reserve;
import com.example.orderMeal.service.DiningTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@ResponseBody
@RestController
@RequestMapping("DiningTable")
public class DiningTableController {
    @Autowired
    DiningTableService diningTableService;

    @GetMapping(value = "/selectAllDTable")
    public List<DiningTable> selectAllDTable(){
        return diningTableService.selectAllDTable();
    }

    @PostMapping(value = "/updateDTable")
    public Boolean updateReserve(@RequestBody DiningTable dTable) {
        return diningTableService.updateDTable(dTable.getTableId(), dTable.getCapacity(), dTable.getStatus(), dTable.getNote());
    }

    @PostMapping(value = "/insertDTable")
    public Boolean insertDTable(@RequestBody DiningTable dTable) {
        return diningTableService.insertDTable(dTable.getTableId(), dTable.getCapacity(), dTable.getStatus(), dTable.getNote());
    }
}
