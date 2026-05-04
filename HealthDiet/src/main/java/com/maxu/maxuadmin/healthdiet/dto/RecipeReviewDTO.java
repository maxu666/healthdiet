package com.maxu.maxuadmin.healthdiet.dto;

import lombok.Data;

@Data
public class RecipeReviewDTO {

    private Long recipeId;

    private Long userId;

    private String content;

    private Integer rating;
}