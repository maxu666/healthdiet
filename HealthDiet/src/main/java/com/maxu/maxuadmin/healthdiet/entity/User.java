package com.maxu.maxuadmin.healthdiet.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String role;

    private String password;

    private String nickname;

    private Integer gender;

    private Integer age;

    private Double height;

    private Double weight;

    private String healthGoal;

    private String dietPreference;

    private Integer status;

    private String activityLevel;
}