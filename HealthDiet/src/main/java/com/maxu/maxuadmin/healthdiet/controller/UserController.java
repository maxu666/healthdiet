package com.maxu.maxuadmin.healthdiet.controller;

import com.maxu.maxuadmin.healthdiet.entity.User;
import com.maxu.maxuadmin.healthdiet.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    // 查询所有用户
    @GetMapping("/list")
    public List<User> list() {
        return userService.list();
    }

    // 根据ID查询用户
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    // 新增用户
    @PostMapping("/add")
    public String add(@RequestBody User user) {
        userService.save(user);
        return "添加成功";
    }

    // 修改用户信息
    @PutMapping("/update")
    public String update(@RequestBody User user) {
        userService.updateById(user);
        return "修改成功";
    }

    // 删除用户
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.removeById(id);
        return "删除成功";
    }

    // 获取所有营养师
    @GetMapping("/nutritionists")
    public List<User> getNutritionists() {
        return userService.getNutritionists();
    }

    // 获取营养师的咨询用户列表
    @GetMapping("/consultUsers/{nutritionistId}")
    public List<User> getConsultUsers(@PathVariable Long nutritionistId) {
        return userService.getConsultUsers(nutritionistId);
    }
}
