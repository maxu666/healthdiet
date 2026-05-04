package com.maxu.maxuadmin.healthdiet.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatSessionVO {

    private Long id;

    private Long userId;

    private Long nutritionistId;

    private String title;

    private Integer status;

    private LocalDateTime lastMessageTime;

    // ⭐ 扩展字段（前端用）
    private String lastMessage;

    private Integer unreadCount;
}
