package com.example.orderMeal;

import com.example.orderMeal.bean.User;
import com.example.orderMeal.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserTest {
    @Autowired
    UserService userService;

    @Test
    void selectByIdTest() {
        userService.selectUserById("1");
    }

    @Test void selectByIdsTest() {
        List<User> userList = null;
        List<String> names = null;
//        for(int i = 0; i < ids.size(); i++) {
//            userList.add(userService.selectUserById(ids.get(i)));
//            names.add(userList.get(i).getName());
//        }
    }
}
