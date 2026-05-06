package com.maxu.maxuadmin.healthdiet.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserNutritionDTO {

    private List<DietRecordItemVO> records;

    private NutritionSummary summary;

    @Data
    public static class DietRecordItemVO {
        private Long id;
        private String mealType;
        private String recordDate;
        private String foodName;
        private String type;
        private Double weight;
        private Double calorie;
        private Double protein;
        private Double fat;
        private Double carbohydrate;
        private Double fiber;
        private Double sodium;
        private Long dishId;
    }

    @Data
    public static class NutritionSummary {
        private Double totalCalorie;
        private Double totalProtein;
        private Double totalFat;
        private Double totalCarbohydrate;
        private Double totalFiber;
        private Double totalSodium;
        private Integer recordCount;
        private Integer foodCount;
    }
}