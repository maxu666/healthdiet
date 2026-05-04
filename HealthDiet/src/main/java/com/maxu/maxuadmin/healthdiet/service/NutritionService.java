package com.maxu.maxuadmin.healthdiet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maxu.maxuadmin.healthdiet.entity.Nutrition;

public interface NutritionService extends IService<Nutrition> {
    Nutrition getByFoodId(Long foodId);

}
