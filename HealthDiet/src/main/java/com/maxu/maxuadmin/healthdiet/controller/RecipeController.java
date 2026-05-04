package com.maxu.maxuadmin.healthdiet.controller;

import com.maxu.maxuadmin.healthdiet.dto.RecipeDTO;
import com.maxu.maxuadmin.healthdiet.entity.Recipe;
import com.maxu.maxuadmin.healthdiet.service.RecipeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/recipe")
public class RecipeController {

    @Resource
    private RecipeService recipeService;

    @PostMapping("/add")
    public void add(@RequestBody RecipeDTO dto) {
        recipeService.addRecipe(dto);
    }

    @GetMapping("/list")
    public List<Recipe> list(Long userId) {
        return recipeService.listByUser(userId);
    }

    @GetMapping("/recommend")
    public List<Recipe> recommend() {
        return recipeService.listAll();
    }

    @GetMapping("/recommend/today")
    public List<Recipe> recommendToday() {
        return recipeService.listAll();
    }

    @GetMapping("/recommend/health")
    public List<Recipe> recommendHealth() {
        return recipeService.listByHealthGoal("健康");
    }

    @GetMapping("/recommend/weightLoss")
    public List<Recipe> recommendWeightLoss() {
        return recipeService.listByHealthGoal("减脂");
    }

    @GetMapping("/recommend/muscleGain")
    public List<Recipe> recommendMuscleGain() {
        return recipeService.listByHealthGoal("增肌");
    }

    @GetMapping("/recommend/nutritionist")
    public List<Recipe> recommendNutritionist() {
        return recipeService.listByNutritionist();
    }

    @GetMapping("/search/nutritionist")
    public List<Recipe> searchNutritionist(@RequestParam String name) {
        return recipeService.searchByNutritionistName(name);
    }

    @GetMapping("/{id}")
    public RecipeDTO detail(@PathVariable Long id) {
        return recipeService.getDetail(id);
    }

    @PutMapping("/update/{id}")
    public void update(@PathVariable Long id, @RequestBody RecipeDTO dto) {
        recipeService.updateRecipe(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
    }

    @GetMapping("/byNutritionist/{nutritionistId}")
    public List<Recipe> getByNutritionist(@PathVariable Long nutritionistId) {
        return recipeService.listByNutritionistId(nutritionistId);
    }
}