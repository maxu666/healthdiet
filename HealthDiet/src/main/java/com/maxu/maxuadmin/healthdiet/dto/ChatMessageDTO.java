package com.maxu.maxuadmin.healthdiet.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {

    private Long sessionId;
    private Long senderId;
    private String senderRole; // USER / NUTRITIONIST
    private String content;
}
