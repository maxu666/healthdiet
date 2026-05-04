package com.maxu.maxuadmin.healthdiet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maxu.maxuadmin.healthdiet.dto.RecipeDTO;
import com.maxu.maxuadmin.healthdiet.entity.Recipe;
import com.maxu.maxuadmin.healthdiet.entity.RecipeItem;
import com.maxu.maxuadmin.healthdiet.mapper.RecipeItemMapper;
import com.maxu.maxuadmin.healthdiet.mapper.RecipeMapper;
import com.maxu.maxuadmin.healthdiet.service.RecipeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Resource
    private RecipeMapper recipeMapper;

    @Resource
    private RecipeItemMapper recipeItemMapper;

    @Override
    @Transactional
    public void addRecipe(RecipeDTO dto) {

        Recipe recipe = new Recipe();
        recipe.setName(dto.getName());
        recipe.setDescription(dto.getDescription());
        recipe.setCreatorId(dto.getUserId());
        recipe.setCreatorType(dto.getCreatorType());
        recipe.setIsPublic(0);
        recipe.setHealthGoal(dto.getHealthGoal() != null ? dto.getHealthGoal() : "健康");
        recipeMapper.insert(recipe);

        for (RecipeDTO.Item item : dto.getItems()) {
            RecipeItem ri = new RecipeItem();
            ri.setRecipeId(recipe.getId());
            ri.setFoodId(item.getFoodId());
            ri.setDishId(item.getDishId());
            ri.setType(item.getType());
            ri.setMealType(item.getMealType());
            ri.setWeight(item.getWeight());

            recipeItemMapper.insert(ri);
        }
    }

    @Override
    public List<Recipe> listByUser(Long userId) {
        return recipeMapper.selectList(
                new QueryWrapper<Recipe>().eq("creator_id", userId)
        );
    }

    @Override
    public List<Recipe> listAll() {
        return recipeMapper.selectList(null);
    }

    @Override
    public RecipeDTO getDetail(Long recipeId) {
        Recipe recipe = recipeMapper.selectById(recipeId);
        if (recipe == null) {
            return null;
        }

        List<RecipeItem> items = recipeItemMapper.selectList(
                new QueryWrapper<RecipeItem>().eq("recipe_id", recipeId)
        );

        RecipeDTO dto = new RecipeDTO();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setHealthGoal(recipe.getHealthGoal());
        dto.setDescription(recipe.getDescription());
        dto.setUserId(recipe.getCreatorId());
        dto.setCreatorType(recipe.getCreatorType());

        List<RecipeDTO.Item> dtoItems = new ArrayList<>();
        for (RecipeItem item : items) {
            RecipeDTO.Item dtoItem = new RecipeDTO.Item();
            dtoItem.setFoodId(item.getFoodId());
            dtoItem.setDishId(item.getDishId());
            dtoItem.setType(item.getType());
            dtoItem.setMealType(item.getMealType());
            dtoItem.setWeight(item.getWeight());
            dtoItems.add(dtoItem);
        }
        dto.setItems(dtoItems);

        return dto;
    }

    @Override
    @Transactional
    public void updateRecipe(Long recipeId, RecipeDTO dto) {
        Recipe recipe = recipeMapper.selectById(recipeId);
        if (recipe != null) {
            recipe.setName(dto.getName());
            recipe.setDescription(dto.getDescription());
            if (dto.getCreatorType() != null) {
                recipe.setCreatorType(dto.getCreatorType());
            }
            if (dto.getHealthGoal() != null) {
                recipe.setHealthGoal(dto.getHealthGoal());
            }
            recipeMapper.updateById(recipe);

            recipeItemMapper.delete(
                    new QueryWrapper<RecipeItem>().eq("recipe_id", recipeId)
            );

            for (RecipeDTO.Item item : dto.getItems()) {
                RecipeItem ri = new RecipeItem();
                ri.setRecipeId(recipeId);
                ri.setFoodId(item.getFoodId());
                ri.setDishId(item.getDishId());
                ri.setType(item.getType());
                ri.setMealType(item.getMealType());
                ri.setWeight(item.getWeight());
                recipeItemMapper.insert(ri);
            }
        }
    }

    @Override
    @Transactional
    public void deleteRecipe(Long recipeId) {
        recipeItemMapper.delete(
                new QueryWrapper<RecipeItem>().eq("recipe_id", recipeId)
        );
        recipeMapper.deleteById(recipeId);
    }

    @Override
    public List<Recipe> listByHealthGoal(String healthGoal) {
        return recipeMapper.selectList(
                new QueryWrapper<Recipe>().eq("health_goal", healthGoal)
        );
    }

    @Override
    public List<Recipe> listByNutritionist() {
        return recipeMapper.selectList(
                new QueryWrapper<Recipe>().eq("creator_type", "NUTRITIONIST")
        );
    }

    @Override
    public List<Recipe> searchByNutritionistName(String name) {
        return recipeMapper.selectList(
                new QueryWrapper<Recipe>()
                        .eq("creator_type", "NUTRITIONIST")
                        .like("name", name)
        );
    }

    @Override
    public List<Recipe> listByNutritionistId(Long nutritionistId) {
        return recipeMapper.selectList(
                new QueryWrapper<Recipe>()
                        .eq("creator_id", nutritionistId)
                        .eq("creator_type", "NUTRITIONIST")
        );
    }
}