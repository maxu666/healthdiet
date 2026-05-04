package com.maxu.maxuadmin.healthdiet.service;

import com.maxu.maxuadmin.healthdiet.dto.RecipeDTO;
import com.maxu.maxuadmin.healthdiet.entity.Recipe;

import java.util.List;

public interface RecipeService {
    void addRecipe(RecipeDTO dto);

    List<Recipe> listByUser(Long userId);

    List<Recipe> listAll();

    RecipeDTO getDetail(Long recipeId);

    void updateRecipe(Long recipeId, RecipeDTO dto);

    void deleteRecipe(Long recipeId);

    List<Recipe> listByHealthGoal(String healthGoal);

    List<Recipe> listByNutritionist();

    List<Recipe> searchByNutritionistName(String name);

    List<Recipe> listByNutritionistId(Long nutritionistId);
}