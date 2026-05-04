package com.maxu.maxuadmin.healthdiet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxu.maxuadmin.healthdiet.dto.FoodDTO;
import com.maxu.maxuadmin.healthdiet.entity.Food;
import com.maxu.maxuadmin.healthdiet.entity.Nutrition;
import com.maxu.maxuadmin.healthdiet.mapper.FoodMapper;
import com.maxu.maxuadmin.healthdiet.mapper.NutritionMapper;
import com.maxu.maxuadmin.healthdiet.service.FoodService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {
    @Resource
    private NutritionMapper nutritionMapper;

    @Override
    @Transactional
    public void addFull(Food food, Nutrition nutrition) {

        // 保存 food
        this.save(food);

        // 拿到 foodId
        Long foodId = food.getId();

        // 绑定 nutrition
        nutrition.setFoodId(foodId);

        // 保存 nutrition
        nutritionMapper.insert(nutrition);
    }

    @Override
    @Transactional
    public void updateFull(Food food, Nutrition nutrition) {
        // 更新 food
        this.updateById(food);

        // 更新 nutrition（根据 foodId 查询并更新）
        nutrition.setFoodId(food.getId());
        nutritionMapper.updateById(nutrition);
    }

    @Override
    public FoodDTO getFullById(Long id) {
        Food food = this.getById(id);
        if (food == null) {
            return null;
        }

        Nutrition nutrition = nutritionMapper.selectOne(
                new LambdaQueryWrapper<Nutrition>()
                        .eq(Nutrition::getFoodId, id)
        );

        FoodDTO dto = new FoodDTO();
        dto.setFood(food);
        dto.setNutrition(nutrition);
        return dto;
    }

    @Override
    public List<FoodDTO> listFull() {
        List<Food> foods = this.list();
        List<FoodDTO> result = new ArrayList<>();

        for (Food food : foods) {
            Nutrition nutrition = nutritionMapper.selectOne(
                    new LambdaQueryWrapper<Nutrition>()
                            .eq(Nutrition::getFoodId, food.getId())
            );

            FoodDTO dto = new FoodDTO();
            dto.setFood(food);
            dto.setNutrition(nutrition);
            result.add(dto);
        }

        return result;
    }

    @Override
    @Transactional
    public boolean removeById(Serializable id) {

        // 1️⃣ 转换 id
        Long foodId = Long.valueOf(id.toString());

        // 2️⃣ 删除 nutrition
        nutritionMapper.delete(
                new LambdaQueryWrapper<Nutrition>()
                        .eq(Nutrition::getFoodId, foodId)
        );

        // 3️⃣ 删除 food
        return super.removeById(id);
    }
}