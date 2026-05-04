package com.maxu.maxuadmin.healthdiet.controller;

import com.maxu.maxuadmin.healthdiet.dto.DietRecordDTO;
import com.maxu.maxuadmin.healthdiet.entity.DietRecord;
import com.maxu.maxuadmin.healthdiet.entity.DietRecordItem;
import com.maxu.maxuadmin.healthdiet.service.DietRecordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/dietRecord")
public class DietRecordController {

    @Resource
    private DietRecordService dietRecordService;

    // 添加一顿饭
    @PostMapping("/add")
    public String add(@RequestBody DietRecordDTO dto) {

        // 1. 取出 record
        DietRecord record = dto.getRecord();

        // 2. 转换 items（DTO → entity）
        List<DietRecordItem> items = new ArrayList<>();

        for (DietRecordDTO.Item i : dto.getItems()) {
            DietRecordItem item = new DietRecordItem();
            item.setFoodId(i.getFoodId());
            item.setDishId(i.getDishId());
            item.setWeight(i.getWeight());
            item.setType(i.getType());
            items.add(item);
        }

        // 3. 调用 service
        dietRecordService.addRecord(record, items);

        return "记录成功";
    }


    @CrossOrigin
    @GetMapping("/list")
    public List<Map<String, Object>> list(
            @RequestParam Long userId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String mealType) {
        return dietRecordService.getRecordListByDate(userId, date, mealType);
    }

    @GetMapping("/group")
    public List<Map<String, Object>> group(@RequestParam Long userId) {
        return dietRecordService.getRecordGroup(userId);
    }

    @GetMapping("/stat")
    public Map<String, Object> stat(@RequestParam Long userId) {
        return dietRecordService.getStat(userId);
    }

    @GetMapping("/trend/stat")
    public Map<String, Object> getTrendStat(
            @RequestParam Long userId,
            @RequestParam String type
    ) {
        return dietRecordService.getTrendStat(userId, type);
    }

    @GetMapping("/trend/line")
    public List<Map<String, Object>> getTrendLine(
            @RequestParam Long userId
    ) {
        return dietRecordService.getTrendLine(userId);
    }

    @GetMapping("/todayCalorie")
    public Map<String, Object> getTodayCalorie(@RequestParam Long userId) {
        return dietRecordService.getTodayCalorieWithTarget(userId);
    }

    @PutMapping("/update")
    public String update(
            @RequestParam Long userId,
            @RequestParam String date,
            @RequestParam String mealType,
            @RequestBody DietRecordDTO dto) {
        dietRecordService.updateRecord(userId, date, mealType, dto);
        return "更新成功";
    }

    @DeleteMapping("/delete")
    public String delete(
            @RequestParam Long userId,
            @RequestParam String date,
            @RequestParam String mealType) {
        dietRecordService.deleteRecord(userId, date, mealType);
        return "删除成功";
    }

    @GetMapping("/userData")
    public com.maxu.maxuadmin.healthdiet.dto.UserNutritionDTO getUserNutrition(@RequestParam Long userId) {
        return dietRecordService.getUserNutrition(userId);
    }
}
