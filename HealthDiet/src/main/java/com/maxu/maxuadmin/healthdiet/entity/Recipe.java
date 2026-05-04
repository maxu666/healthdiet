package com.maxu.maxuadmin.healthdiet.entity;

import lombok.Data;

@Data
public class Recipe {
    private Long id;
    private String name;
    private String description;
    private Long creatorId;
    private String creatorType;
    private Integer isPublic;
    private String healthGoal;
}