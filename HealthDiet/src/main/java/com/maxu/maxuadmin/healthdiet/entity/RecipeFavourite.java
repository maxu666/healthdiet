package com.maxu.maxuadmin.healthdiet.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("recipe_favorite")
public class RecipeFavourite {
    private Long id;
    private Long userId;
    private Long recipeId;
    private Date createTime;
}
