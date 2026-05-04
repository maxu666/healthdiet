package com.maxu.maxuadmin.healthdiet.service;

import com.maxu.maxuadmin.healthdiet.dto.RecipeReviewDTO;
import com.maxu.maxuadmin.healthdiet.vo.RecipeReviewVO;

import java.util.List;

public interface RecipeReviewService {
    void addReview(RecipeReviewDTO dto);
    List<RecipeReviewVO> listByRecipe(long recipeId);
    void deleteReview(Long reviewId, Long userId);
    Double getAverageRating(Long recipeId);
    int getReviewCount(Long recipeId);
}
