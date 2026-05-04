package com.maxu.maxuadmin.healthdiet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("food")
public class Food {

    @TableId(type = IdType.AUTO)

    private Long id;

    private String name;

    private String category;

    private String imageUrl;

    private LocalDateTime createTime;
}
