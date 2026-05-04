package com.maxu.maxuadmin.healthdiet.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecipeDTO {
    private Long id;
    private String name;
    private String healthGoal;
    private String description;
    private Long userId;
    private String creatorType;
    private List<Item> items;

    @Data
    public static class Item {
        private Long foodId;
        private Long dishId;
        private String type;
        private String mealType;
        private Double weight;
    }
}