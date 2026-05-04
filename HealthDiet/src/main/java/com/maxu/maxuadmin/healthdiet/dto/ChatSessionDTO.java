package com.maxu.maxuadmin.healthdiet.dto;
import lombok.Data;

@Data
public class ChatSessionDTO {
    private Long id;
    private Long userId;
    private Long nutritionistId;
    private String title;
}