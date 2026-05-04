package com.maxu.maxuadmin.healthdiet.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("recipe_review")
public class RecipeReview {
    private Long id;
    private Long recipeId;
    private Long userId;
    private Integer rating;
    private String content;
    private LocalDateTime createTime;
}
