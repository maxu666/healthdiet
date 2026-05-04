package com.maxu.maxuadmin.healthdiet.entity;

import lombok.Data;

@Data
public class RecipeItem {
    private Long id;
    private Long recipeId;
    private Long foodId;
    private Long dishId;
    private String type;
    private String mealType;
    private Double weight;
}
