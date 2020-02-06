package com.example.orderMeal.service;

import com.example.orderMeal.bean.User;
import com.example.orderMeal.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public List<User> selectUserExceptAdmin() {
        return userMapper.selectUserExceptAdmin();
    }

    public User selectUserById(String id) {
        return userMapper.selectUserById(id);
    }

    public List<User> screenUser(String name, String minCredit, String maxCredit, String type) {
        return userMapper.screenUser(name, minCredit, maxCredit, type);
    }

    public User userLogin(String name, String passwd, String type) {
        return userMapper.userLogin(name, passwd, type);
    }

    public List<User> selectAllUser() {
        return userMapper.selectAllUser();
    }

    public Boolean updateUser(String id, int credit, int type) {
        return userMapper.updateUser(id, credit, type);
    }

    public Boolean delUser(String id) {
        return userMapper.delUser(id);
    }
}
