package com.maxu.maxuadmin.healthdiet.dto;

import com.maxu.maxuadmin.healthdiet.entity.Food;
import com.maxu.maxuadmin.healthdiet.entity.Nutrition;
import lombok.Data;

@Data
public class FoodDTO {

    private Food food;

    private Nutrition nutrition;
}
