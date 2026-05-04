package com.maxu.maxuadmin.healthdiet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("chat_session")
public class ChatSession {
    private Long id;

    private Long userId;
    private Long nutritionistId;

    private String title;

    private Integer status; // 0进行中 1结束

    private LocalDateTime lastMessageTime;

    private LocalDateTime createTime;
}
