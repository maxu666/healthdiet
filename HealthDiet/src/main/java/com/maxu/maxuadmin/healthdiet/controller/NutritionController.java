package com.maxu.maxuadmin.healthdiet.controller;

import com.maxu.maxuadmin.healthdiet.entity.Nutrition;
import com.maxu.maxuadmin.healthdiet.service.NutritionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/nutrition")
public class NutritionController {

    @Resource
    private NutritionService nutritionService;

    //根据foodId查询营养
    @GetMapping("/{foodId}")
    public Nutrition getByFoodId(@PathVariable Long foodId) {
        return nutritionService.getByFoodId(foodId);
    }


}