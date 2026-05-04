package com.maxu.maxuadmin.healthdiet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("chat_message")
public class ChatMessage {

    private Long id;

    private Long sessionId;

    private Long senderId;

    private String senderRole; // USER / NUTRITIONIST

    private String content;

    private Integer isRead; // 0未读 1已读

    private LocalDateTime createTime;
}