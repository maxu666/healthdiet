package com.maxu.maxuadmin.healthdiet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("diet_record")
public class DietRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    // 日期
    private LocalDate recordDate;

    // 早餐/午餐/晚餐
    private String mealType;

    // 备注
    private String remark;

    private LocalDateTime createTime;
}
