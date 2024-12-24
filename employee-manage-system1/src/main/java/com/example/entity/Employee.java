package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("employee")
public class Employee {
    private Long id;
    private String number;
    private String name;
    private String bumen;
    private String zhiwei;
    private String birth;
    private String sex;
    private Long salary;
    private String permission;
} 