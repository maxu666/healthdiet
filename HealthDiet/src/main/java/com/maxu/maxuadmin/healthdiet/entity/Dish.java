package com.maxu.maxuadmin.healthdiet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Dish {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String category;

    private String imageUrl;

    private String description;

    private Long creatorId;

    private String creatorRole;

    private Integer isDeleted;

    private LocalDateTime createTime;
}
