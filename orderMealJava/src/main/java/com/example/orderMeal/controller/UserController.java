package com.example.orderMeal.controller;

import com.example.orderMeal.bean.User;
import com.example.orderMeal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@ResponseBody
@RequestMapping("User")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping(value = "/selectUserExceptAdmin")
    public List<User> selectUserExceptAdmin() {
        return userService.selectUserExceptAdmin();
    }

    @GetMapping(value = "/selectAllUser")
    public List<User> selectAllUser() {
        return userService.selectAllUser();
    }

    @PostMapping(value = "/selectUserById")
    public User selectUserById(@RequestParam(value = "id") String id) {
        return userService.selectUserById(id);
    }

    @PostMapping(value = "/selectUserByIds")
    public List<String> selectUserByIds(@RequestParam(value = "ids[]") String[] ids) {
        List<String> names = new LinkedList<String> ();
        for(int i = 0; i < ids.length; i++) {
            names.add(userService.selectUserById(ids[i]).getName());
        }
        return names;
    }

    @PostMapping(value = "/screenUser")
    public List<User> screenUser(@RequestBody Map<String, Object> screenData) {
        String name, minCredit, maxCredit, type;
        name = minCredit = maxCredit = type = null;
        if(notNull(screenData.get("name"))) {
            name = screenData.get("name").toString();
        }
        if(notNull(screenData.get("minCredit"))) {
            minCredit = screenData.get("minCredit").toString();
        }
        if(notNull(screenData.get("maxCredit"))) {
            maxCredit = screenData.get("maxCredit").toString();
        }
        if(notNull(screenData.get("type"))) {
            type = screenData.get("type").toString();
        }
        return userService.screenUser(name, minCredit, maxCredit, type);
    }

    @PostMapping(value = "/userLogin")
    public Map<String, Object> userLogin(@RequestParam(value = "name") String name,
                                         @RequestParam(value = "passwd") String passwd,
                                         @RequestParam(value = "type") String type) {
        User user = userService.userLogin(name, passwd, type == "" ? null : type);
        if(user != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", user.getId());
            map.put("name", user.getName());
            return map;
        }
        return null;
    }

    @PostMapping(value = "updateUser")
    private Boolean updateUser(@RequestBody Map<String, Object> user) {
        String id = user.get("id").toString();
        int credit = Integer.parseInt(user.get("credit").toString());
        int type = Integer.parseInt(user.get("type").toString());
        return userService.updateUser(id, credit, type);
    }

    @PostMapping(value = "delUser")
    private Boolean delUser(@RequestParam(value = "id") String id) {
        return userService.delUser(id);
    }

    private boolean notNull(Object object) {
        return object == null ? false : true;
    }
}
