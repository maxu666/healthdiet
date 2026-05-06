package com.maxu.maxuadmin.healthdiet.dto;

import lombok.Data;

import java.util.List;

@Data
public class NutritionistDashboardDTO {
    // 今日咨询数
    private Integer todayConsultationCount;
    // 待回复数量
    private Integer pendingReplyCount;
    // 服务用户数
    private Integer servedUserCount;
    // 待回复咨询列表
    private List<PendingConsultation> pendingConsultations;
    // 食谱统计
    private RecipeStats recipeStats;
    // 本周服务数据
    private WeeklyStats weeklyStats;

    @Data
    public static class PendingConsultation {
        private Long sessionId;
        private Long userId;
        private String userName;
        private String lastMessage;
        private String messageTime;
    }

    @Data
    public static class RecipeStats {
        private Integer publishedCount;
        private Integer totalFavourites;
        private Double averageRating;
    }

    @Data
    public static class WeeklyStats {
        private Integer weeklyConsultationCount;
        private Integer replyRate;
    }
}
