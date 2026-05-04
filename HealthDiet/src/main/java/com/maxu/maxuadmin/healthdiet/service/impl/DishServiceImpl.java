package com.maxu.maxuadmin.healthdiet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxu.maxuadmin.healthdiet.entity.Dish;
import com.maxu.maxuadmin.healthdiet.entity.DishItem;
import com.maxu.maxuadmin.healthdiet.entity.Food;
import com.maxu.maxuadmin.healthdiet.mapper.DishItemMapper;
import com.maxu.maxuadmin.healthdiet.mapper.DishMapper;
import com.maxu.maxuadmin.healthdiet.mapper.FoodMapper;
import com.maxu.maxuadmin.healthdiet.service.DishService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DishServiceImpl implements DishService {

    @Resource
    private DishMapper dishMapper;

    @Resource
    private DishItemMapper dishItemMapper;

    @Resource
    private FoodMapper foodMapper;

    /**
     * 新增菜品
     */
    @Override
    @Transactional
    public void addFull(Dish dish, List<DishItem> items) {

        // 1️⃣ 保存菜品
        dishMapper.insert(dish);

        // 获取生成的 dishId
        Long dishId = dish.getId();

        // 2️⃣ 保存食材
        for (DishItem item : items) {
            item.setDishId(dishId);
            dishItemMapper.insert(item);
        }
    }

    /**
     * 查询所有菜品
     */
    @Override
    public List<Dish> list() {
        return dishMapper.selectList(null);
    }

    /**
     * 查询菜品详情（带食材）
     */
    @Override
    public Map<String, Object> getDetail(Long dishId) {

        // 查询菜品
        Dish dish = dishMapper.selectById(dishId);

        // 查询食材
        List<DishItem> items = dishItemMapper.selectList(
                new LambdaQueryWrapper<DishItem>()
                        .eq(DishItem::getDishId, dishId)
        );

        // 构建包含食材名称的结果列表
        List<Map<String, Object>> itemsWithName = new ArrayList<>();
        for (DishItem item : items) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("foodId", item.getFoodId());
            itemMap.put("weight", item.getWeight());
            if (item.getFoodId() != null) {
                Food food = foodMapper.selectById(item.getFoodId());
                if (food != null) {
                    itemMap.put("foodName", food.getName());
                }
            }
            itemsWithName.add(itemMap);
        }

        // 组装返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("dish", dish);
        result.put("items", itemsWithName);

        return result;
    }

    /**
     * 删除菜品
     */
    @Override
    @Transactional
    public void deleteById(Long dishId) {

        // 删除菜品
        dishMapper.deleteById(dishId);

        // 删除关联食材
        dishItemMapper.delete(
                new LambdaQueryWrapper<DishItem>()
                        .eq(DishItem::getDishId, dishId)
        );
    }

    /**
     * 更新菜品（重点🔥）
     */
    @Override
    @Transactional
    public void updateFull(Dish dish, List<DishItem> items) {

        // 更新菜品
        dishMapper.updateById(dish);

        Long dishId = dish.getId();

        // 删除旧食材
        dishItemMapper.delete(
                new LambdaQueryWrapper<DishItem>()
                        .eq(DishItem::getDishId, dishId)
        );

        // 插入新食材
        for (DishItem item : items) {
            item.setDishId(dishId);
            dishItemMapper.insert(item);
        }
    }

    /**
     * 根据创建者ID查询菜品
     */
    @Override
    public List<Dish> listByCreator(Long creatorId) {
        return dishMapper.selectList(
                new LambdaQueryWrapper<Dish>()
                        .eq(Dish::getCreatorId, creatorId)
                        .eq(Dish::getIsDeleted, 0)
        );
    }
}