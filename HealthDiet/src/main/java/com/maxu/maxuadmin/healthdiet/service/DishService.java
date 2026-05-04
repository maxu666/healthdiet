package com.maxu.maxuadmin.healthdiet.service;

import com.maxu.maxuadmin.healthdiet.entity.Dish;
import com.maxu.maxuadmin.healthdiet.entity.DishItem;

import java.util.List;
import java.util.Map;

public interface DishService {

    /**
     * ✅ 新增菜品（菜品 + 食材）
     */
    void addFull(Dish dish, List<DishItem> items);

    /**
     * ✅ 查询所有菜品
     */
    List<Dish> list();

    /**
     * ✅ 查询菜品详情（包含食材）
     */
    Map<String, Object> getDetail(Long dishId);

    /**
     * ✅ 删除菜品
     */
    void deleteById(Long dishId);

    /**
     * ✅ 更新菜品（菜品 + 食材）
     */
    void updateFull(Dish dish, List<DishItem> items);

    /**
     * ✅ 根据创建者ID查询菜品
     */
    List<Dish> listByCreator(Long creatorId);
}
