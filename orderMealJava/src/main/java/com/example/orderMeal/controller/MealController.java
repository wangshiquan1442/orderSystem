package com.example.orderMeal.controller;

import com.example.orderMeal.bean.Meal;
import com.example.orderMeal.config.FtpUtil;
import com.example.orderMeal.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("Meal")
public class MealController {
    @Autowired
    MealService mealService;
    @Value("${FTP.ADDRESS}")
    private String host;    // 端口
    @Value("${FTP.PORT}")
    private int port;    // ftp用户名
    @Value("${FTP.USERNAME}")
    private String userName;    // ftp用户密码
    @Value("${FTP.PASSWORD}")
    private String passWord;    // 文件在服务器端保存的主目录
    @Value("${FTP.BASEPATH}")
    private String basePath;    // 访问图片时的基础url
    @Value("${IMAGE.BASE.URL}")
    private String imgUrl;

    @ResponseBody
    @GetMapping(value = "/selectAllMeal")
    public List<Meal> selectAllMeal() {
        return mealService.selectAllMeal();
    }

    @ResponseBody
    @PostMapping(value = "/selectMealById")
    public Meal selectAllMealById(@RequestParam(value = "id") int id) {
        return mealService.selectMealById(id);
    }

    @PostMapping(value = "/searchMeal")
    public List<Meal> selectMealByName(@RequestParam(value = "name") String name) {
        return mealService.selectMealByName(name);
    }

    @PostMapping(value = "/getMealList")
    public List<Meal> getMealList(@RequestParam(value = "mealList") String idList) {
        List<Meal> mealList = new LinkedList<Meal>();
        String id = "";
        int x = 0;
        for (int i = 0; i < idList.length(); i++) {
            if (idList.charAt(i) == ',') {
                for (int j = x; j < i; j++) {
                    id += String.valueOf(idList.charAt(j));
                }
                mealList.add(mealService.selectMealById(Integer.valueOf(id)));
                id = "";
                x = i + 1;
            }
        }
        return mealList;
    }

    @PostMapping(value = "/uploadImg")
    public Boolean uploadImg(@RequestParam("pic") MultipartFile uploadFile,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("mealId") String mealId) {
        try {
            //3、把图片上传到图片服务器
            //3.1获取上传的io流
            InputStream input = uploadFile.getInputStream();
            //3.2调用FtpUtil工具类进行上传
            String fileName = mealId + ".png";
            boolean result = FtpUtil.uploadFile(host, port, userName, passWord, basePath, imgUrl, fileName, input);
            if (result) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    @GetMapping(value = "/selectMealCate")
    public List<Map<String, Object>> selectMealCate() {
        return mealService.selectMealCate();
    }

    @PostMapping(value = "/insertMeal")
    public Boolean insertMeal(@RequestBody Meal meal) {
        return mealService.insertMeal(meal.getName(), meal.getDesc(), meal.getCategory(), meal.getPrice());
    }

    @PostMapping(value = "/updateMeal")
    public Boolean updateMeal(@RequestBody Meal meal) {
        return mealService.updateMeal(meal.getId(), meal.getName(), meal.getDesc(), meal.getCategory(), meal.getPrice());
    }

    @PostMapping(value = "/delMeal")
    public Boolean delMeal(@RequestParam(value = "id") String id) {
        return mealService.delMeal(id);
    }
}
