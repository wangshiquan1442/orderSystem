package com.example.orderMeal.controller;

import com.example.orderMeal.config.WebSocketServer;
import com.example.orderMeal.service.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("Preference")
public class PreferenceController {
    @Autowired
    private PreferenceService preferenceService;
    @Autowired
    private WebSocketServer webSocketServer;

    @ResponseBody
    @GetMapping(value = "/selectDiscount")
    public List<Map<String, Object>> selectDiscount() {
        return preferenceService.selectDiscount();
    }

    @ResponseBody
    @PostMapping(value = "/sendToUsers")
    public void sendToUsers(@RequestParam(value = "msg") String msg) {
        try {
            webSocketServer.onMessage(msg);
        }catch (Exception e) {

        }
    }
}
