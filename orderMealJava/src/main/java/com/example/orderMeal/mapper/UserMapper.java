package com.example.orderMeal.mapper;

import com.example.orderMeal.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> selectUserExceptAdmin();

    User selectUserById(String id);

    List<User> screenUser(String name,  String minCredit, String maxCredit, String type);

    User userLogin(String name, String passwd, String type);

    List<User> selectAllUser();

    Boolean updateUser(String id, int credit, int type);

    Boolean delUser(String id);
}
