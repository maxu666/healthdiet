package com.maxu.maxuadmin.healthdiet.controller;

import com.maxu.maxuadmin.healthdiet.dto.NutritionistDashboardDTO;
import com.maxu.maxuadmin.healthdiet.service.NutritionistDashboardService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nutritionistDashboard")
@CrossOrigin
public class NutritionistDashboardController {

    @Resource
    private NutritionistDashboardService dashboardService;

    @GetMapping("/get/{nutritionistId}")
    public NutritionistDashboardDTO getDashboard(@PathVariable Long nutritionistId) {
        return dashboardService.getDashboardData(nutritionistId);
    }
}
