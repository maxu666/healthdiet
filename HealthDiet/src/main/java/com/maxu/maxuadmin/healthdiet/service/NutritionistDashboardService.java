package com.maxu.maxuadmin.healthdiet.service;

import com.maxu.maxuadmin.healthdiet.dto.NutritionistDashboardDTO;

public interface NutritionistDashboardService {
    NutritionistDashboardDTO getDashboardData(Long nutritionistId);
}
