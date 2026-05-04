package com.maxu.maxuadmin.healthdiet.service;

import com.maxu.maxuadmin.healthdiet.dto.DietRecordDTO;
import com.maxu.maxuadmin.healthdiet.entity.DietRecord;
import com.maxu.maxuadmin.healthdiet.entity.DietRecordItem;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface DietRecordService {

    void addRecord(DietRecord record, List<DietRecordItem> items);
    List<Map<String, Object>> getRecordList(Long userId);
    List<Map<String, Object>> getRecordListByDate(Long userId, String date, String mealType);
    //分组饮食记录查询
    List<Map<String, Object>> getRecordGroup(Long userId);
    Map<String, Object> getStat(Long userId);

    Map<String, Object> getTrendStat(Long userId, String type);

    List<Map<String, Object>> getTrendLine(Long userId);

    Map<String, Object> getTodayCalorieWithTarget(Long userId);

    void updateRecord(Long userId, String date, String mealType, DietRecordDTO dto);
    void deleteRecord(Long userId, String date, String mealType);

    com.maxu.maxuadmin.healthdiet.dto.UserNutritionDTO getUserNutrition(Long userId);

    List<com.maxu.maxuadmin.healthdiet.entity.DietRecord> listByUser(Long userId);
}
