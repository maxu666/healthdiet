package com.maxu.maxuadmin.healthdiet.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("nutrition")
public class Nutrition {

    private Long id;

    private Long foodId;

    // 每100g营养数据
    private Double calorie;

    private Double protein;

    private Double fat;

    private Double carbohydrate;

    private Double fiber;

    private Double sodium;

    private LocalDateTime createTime;
}
