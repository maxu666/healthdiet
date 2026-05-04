package com.maxu.maxuadmin.healthdiet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maxu.maxuadmin.healthdiet.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    // 登录方法
    User login(User user);
    // 查询用户信息
    User getUserInfo(Long id);
    // 更新用户信息
    boolean updateUser(User user);
    // 获取所有营养师
    List<User> getNutritionists();
    // 获取营养师的咨询用户列表
    List<User> getConsultUsers(Long nutritionistId);
}
