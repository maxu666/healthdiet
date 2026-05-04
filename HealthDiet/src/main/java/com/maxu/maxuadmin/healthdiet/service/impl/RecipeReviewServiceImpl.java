package com.maxu.maxuadmin.healthdiet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxu.maxuadmin.healthdiet.dto.RecipeReviewDTO;
import com.maxu.maxuadmin.healthdiet.entity.RecipeReview;
import com.maxu.maxuadmin.healthdiet.mapper.RecipeReviewMapper;
import com.maxu.maxuadmin.healthdiet.service.RecipeReviewService;
import com.maxu.maxuadmin.healthdiet.vo.RecipeReviewVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeReviewServiceImpl implements RecipeReviewService {
    @Resource
    private RecipeReviewMapper recipeReviewMapper;
    @Override
    public void addReview(RecipeReviewDTO dto) {
        RecipeReview review = new RecipeReview();
        review.setRecipeId(dto.getRecipeId());
        review.setUserId(dto.getUserId());
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        review.setCreateTime(LocalDateTime.now());
        recipeReviewMapper.insert(review);
    }

    @Override
    public List<RecipeReviewVO> listByRecipe(long recipeId) {

        List<RecipeReview> list = recipeReviewMapper.selectList(
                new LambdaQueryWrapper<RecipeReview>()
                        .eq(RecipeReview::getRecipeId, recipeId)
                        .orderByDesc(RecipeReview::getCreateTime)
        );

        return list.stream().map(r -> {
            RecipeReviewVO vo = new RecipeReviewVO();
            vo.setId(r.getId());
            vo.setRecipeId(r.getRecipeId());
            vo.setUserId(r.getUserId());
            vo.setContent(r.getContent());
            vo.setRating(r.getRating());
            vo.setCreateTime(r.getCreateTime());

            // 先简单处理用户名
            vo.setUsername("用户" + r.getUserId());

            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) {
        if (reviewId == null || userId == null) {
            return;
        }
        RecipeReview review = recipeReviewMapper.selectById(reviewId);
        if (review != null && review.getUserId().equals(userId)) {
            recipeReviewMapper.deleteById(reviewId);
        }
    }

    @Override
    public Double getAverageRating(Long recipeId) {
        if (recipeId == null) {
            return 0.0;
        }
        List<RecipeReview> list = recipeReviewMapper.selectList(
                new LambdaQueryWrapper<RecipeReview>()
                        .eq(RecipeReview::getRecipeId, recipeId)
        );

        if (list.isEmpty()) {
            return 0.0;
        }

        double sum = list.stream().mapToInt(RecipeReview::getRating).sum();
        double average = sum / list.size();

        return Math.round(average * 10) / 10.0;
    }

    @Override
    public int getReviewCount(Long recipeId) {
        if (recipeId == null) {
            return 0;
        }
        return Math.toIntExact(recipeReviewMapper.selectCount(
                new LambdaQueryWrapper<RecipeReview>()
                        .eq(RecipeReview::getRecipeId, recipeId)
        ));
    }
}
