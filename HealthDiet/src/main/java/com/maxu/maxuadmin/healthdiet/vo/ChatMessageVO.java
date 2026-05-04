package com.maxu.maxuadmin.healthdiet.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageVO {

    private Long id;
    private Long sessionId;
    private Long senderId;
    private String senderRole;
    private String content;
    private Integer isRead;
    private LocalDateTime createTime;
}