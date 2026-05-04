package com.maxu.maxuadmin.healthdiet.controller;

import com.maxu.maxuadmin.healthdiet.service.RecipeFavouriteService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/favourite")
public class RecipeFavouriteController {

    @Resource
    private RecipeFavouriteService service;

    // 收藏
    @PostMapping("/add")
    public void add(Long userId, Long recipeId) {
        service.add(userId, recipeId);
    }

    // 取消收藏
    @DeleteMapping("/remove")
    public void remove(Long userId, Long recipeId) {
        service.remove(userId, recipeId);
    }

    // 是否收藏
    @GetMapping("/check")
    public boolean check(Long userId, Long recipeId) {
        return service.isFavorite(userId, recipeId);
    }

    // 我的收藏
    @GetMapping("/list")
    public List<Long> list(Long userId) {
        return service.listByUser(userId);
    }
}
