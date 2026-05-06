package com.maxu.maxuadmin.healthdiet.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageVO {
    private Long id;
    private String type; // ANNOUNCEMENT / CONSULTATION
    private String title;
    private String content;
    private LocalDateTime createTime;
    private Boolean isRead;
    private String senderName; // 发送者名称，用于咨询消息
}
