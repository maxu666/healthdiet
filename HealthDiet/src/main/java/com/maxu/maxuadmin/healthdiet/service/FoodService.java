package com.maxu.maxuadmin.healthdiet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maxu.maxuadmin.healthdiet.dto.FoodDTO;
import com.maxu.maxuadmin.healthdiet.entity.Food;
import com.maxu.maxuadmin.healthdiet.entity.Nutrition;

import java.util.List;

public interface FoodService extends IService<Food> {
    void addFull(Food food, Nutrition nutrition);

    void updateFull(Food food, Nutrition nutrition);

    FoodDTO getFullById(Long id);

    List<FoodDTO> listFull();
}
