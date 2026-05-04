package com.maxu.maxuadmin.healthdiet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maxu.maxuadmin.healthdiet.entity.RecipeFavourite;
import com.maxu.maxuadmin.healthdiet.mapper.RecipeFavouriteMapper;
import com.maxu.maxuadmin.healthdiet.service.RecipeFavouriteService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeFavouriteServiceImpl implements RecipeFavouriteService {

    @Resource
    private RecipeFavouriteMapper mapper;

    @Override
    public void add(Long userId, Long recipeId) {
        // 防止重复收藏
        QueryWrapper<RecipeFavourite> qw = new QueryWrapper<>();
        qw.eq("user_id", userId).eq("recipe_id", recipeId);

        if (mapper.selectCount(qw) == 0) {
            RecipeFavourite f = new RecipeFavourite();
            f.setUserId(userId);
            f.setRecipeId(recipeId);
            mapper.insert(f);
        }
    }

    @Override
    public void remove(Long userId, Long recipeId) {
        QueryWrapper<RecipeFavourite> qw = new QueryWrapper<>();
        qw.eq("user_id", userId).eq("recipe_id", recipeId);
        mapper.delete(qw);
    }

    @Override
    public boolean isFavorite(Long userId, Long recipeId) {
        QueryWrapper<RecipeFavourite> qw = new QueryWrapper<>();
        qw.eq("user_id", userId).eq("recipe_id", recipeId);
        return mapper.selectCount(qw) > 0;
    }

    @Override
    public List<Long> listByUser(Long userId) {
        QueryWrapper<RecipeFavourite> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);

        return mapper.selectList(qw)
                .stream()
                .map(RecipeFavourite::getRecipeId)
                .toList();
    }
}
