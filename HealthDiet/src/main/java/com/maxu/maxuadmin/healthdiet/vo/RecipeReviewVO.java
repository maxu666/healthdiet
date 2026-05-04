package com.maxu.maxuadmin.healthdiet.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecipeReviewVO {

    private Long id;

    private Long recipeId;

    private Long userId;

    private String content;

    private Integer rating;

    private String username; // 展示用

    private LocalDateTime createTime;
}