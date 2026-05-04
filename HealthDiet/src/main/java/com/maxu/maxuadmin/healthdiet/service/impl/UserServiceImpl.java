package com.maxu.maxuadmin.healthdiet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxu.maxuadmin.healthdiet.entity.User;
import com.maxu.maxuadmin.healthdiet.mapper.UserMapper;
import com.maxu.maxuadmin.healthdiet.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User login(User user) {

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        wrapper.eq("password", user.getPassword());
        wrapper.eq("role", user.getRole());

        return userMapper.selectOne(wrapper);
    }

    @Override
    public User getUserInfo(Long id) {
        return this.getById(id);
    }

    @Override
    public boolean updateUser(User user) {
        return this.updateById(user);
    }

    @Override
    public List<User> getNutritionists() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("role", "NUTRITIONIST");
        return this.list(wrapper);
    }

    @Override
    public List<User> getConsultUsers(Long nutritionistId) {
        // 通过会话表查询与该营养师有会话的用户
        return userMapper.selectConsultUsers(nutritionistId);
    }

}