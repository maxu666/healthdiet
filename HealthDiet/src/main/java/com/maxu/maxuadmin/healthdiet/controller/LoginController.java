package com.maxu.maxuadmin.healthdiet.controller;

import com.maxu.maxuadmin.healthdiet.entity.User;
import com.maxu.maxuadmin.healthdiet.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping
@CrossOrigin   // 🔥 解决前端跨域问题
public class LoginController {

    @Resource
    private UserService userService;

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public Object login(@RequestBody User user) {

        // 1️⃣ 调用 service 查询数据库
        User dbUser = userService.login(user);

        // 2️⃣ 判断是否存在
        if (dbUser != null) {

            // 3️⃣ 模拟生成 token（后面可以升级 JWT）
            String token = UUID.randomUUID().toString();

            // 4️⃣ 返回前端数据
            Map<String, Object> result = new HashMap<>();
            result.put("id", dbUser.getId());
            result.put("username", dbUser.getUsername());
            result.put("nickname", dbUser.getNickname());
            result.put("role", dbUser.getRole());
            result.put("token", token);

            return result;

        } else {
            // 5️⃣ 登录失败
            throw new RuntimeException("用户名或密码错误");
        }
    }
}