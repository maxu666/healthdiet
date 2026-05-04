package com.maxu.maxuadmin.healthdiet.controller;

import com.maxu.maxuadmin.healthdiet.dto.RecipeReviewDTO;
import com.maxu.maxuadmin.healthdiet.service.RecipeReviewService;
import com.maxu.maxuadmin.healthdiet.vo.RecipeReviewVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipereview")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class RecipeReviewController {

    @Resource
    private RecipeReviewService recipeReviewService;

    // 新增评论
    @PostMapping("/add")
    public void add(@RequestBody RecipeReviewDTO dto) {
        recipeReviewService.addReview(dto);
    }

    // 查询某个食谱的评论（支持路径参数）
    @GetMapping("/list/{recipeId}")
    public List<RecipeReviewVO> listById(@PathVariable Long recipeId) {
        return recipeReviewService.listByRecipe(recipeId);
    }

    // 查询某个食谱的评论（支持请求参数）
    @GetMapping("/list")
    public List<RecipeReviewVO> listByParam(@RequestParam Long recipeId) {
        return recipeReviewService.listByRecipe(recipeId);
    }

    // 删除评论
    @DeleteMapping("/delete")
    public void delete(@RequestParam Long reviewId, @RequestParam Long userId) {
        recipeReviewService.deleteReview(reviewId, userId);
    }

    // 获取食谱平均评分
    @GetMapping("/averageRating")
    public Double getAverageRating(@RequestParam Long recipeId) {
        return recipeReviewService.getAverageRating(recipeId);
    }

    // 获取食谱评论数量
    @GetMapping("/count")
    public int getReviewCount(@RequestParam Long recipeId) {
        return recipeReviewService.getReviewCount(recipeId);
    }
}