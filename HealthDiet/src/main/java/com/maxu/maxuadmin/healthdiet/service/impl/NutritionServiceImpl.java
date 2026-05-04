package com.maxu.maxuadmin.healthdiet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxu.maxuadmin.healthdiet.entity.Nutrition;
import com.maxu.maxuadmin.healthdiet.entity.User;
import com.maxu.maxuadmin.healthdiet.mapper.DietRecordMapper;
import com.maxu.maxuadmin.healthdiet.mapper.NutritionMapper;
import com.maxu.maxuadmin.healthdiet.mapper.UserMapper;
import com.maxu.maxuadmin.healthdiet.service.NutritionService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NutritionServiceImpl extends ServiceImpl<NutritionMapper, Nutrition> implements NutritionService {

    @Resource
    private NutritionMapper nutritionMapper;
    @Autowired
    private DietRecordMapper dietRecordMapper;

    @Override
    public Nutrition getByFoodId(Long foodId) {
        QueryWrapper<Nutrition> wrapper = new QueryWrapper<>();
        wrapper.eq("food_id", foodId);
        return nutritionMapper.selectOne(wrapper);
    }


}
