package com.maxu.maxuadmin.healthdiet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("diet_record_item")
public class DietRecordItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long recordId;

    private Long foodId;
    private Long dishId;

    private Double weight;

    private Double calorie;
    private Double protein;
    private Double fat;
    private Double carbohydrate;
    private Double fiber;
    private Double sodium;

    // 区分food/dish
    private String type;
}