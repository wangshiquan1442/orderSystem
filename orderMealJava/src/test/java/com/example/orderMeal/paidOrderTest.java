package com.example.orderMeal;

import com.example.orderMeal.bean.PaidOrder;
import com.example.orderMeal.service.PaidOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@SpringBootTest
public class paidOrderTest {
    @Autowired
    PaidOrderService paidOrderService;

    @Test
    public void selectByUidTest() {
        List<PaidOrder> paidOrderList = paidOrderService.selectPaidOrderByUid("1");
        for (PaidOrder paidOrder: paidOrderList
             ) {
            System.out.println(paidOrder.getDate());
            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df2.setTimeZone(TimeZone.getTimeZone("GMT"));
            System.out.println(df2.format(paidOrder.getDate()));
        }
    }

    @Test
    public void insertTest() {
        Date date = new Date();
        paidOrderService.insertPaidOrder("1", "1", date, "5");
    }
}
