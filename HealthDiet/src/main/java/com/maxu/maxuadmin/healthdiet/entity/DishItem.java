package com.maxu.maxuadmin.healthdiet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DishItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long dishId;

    private Long foodId;

    private Double weight;

    private LocalDateTime createTime;
}