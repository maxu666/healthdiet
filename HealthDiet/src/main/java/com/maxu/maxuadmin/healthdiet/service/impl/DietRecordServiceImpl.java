package com.maxu.maxuadmin.healthdiet.service.impl;

import com.maxu.maxuadmin.healthdiet.dto.DietRecordDTO;
import com.maxu.maxuadmin.healthdiet.entity.DietRecord;
import com.maxu.maxuadmin.healthdiet.entity.DietRecordItem;
import com.maxu.maxuadmin.healthdiet.entity.Nutrition;
import com.maxu.maxuadmin.healthdiet.entity.User;
import com.maxu.maxuadmin.healthdiet.mapper.DietRecordItemMapper;
import com.maxu.maxuadmin.healthdiet.mapper.DietRecordMapper;
import com.maxu.maxuadmin.healthdiet.mapper.DishItemMapper;
import com.maxu.maxuadmin.healthdiet.mapper.UserMapper;
import com.maxu.maxuadmin.healthdiet.service.DietRecordService;
import com.maxu.maxuadmin.healthdiet.service.NutritionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DietRecordServiceImpl implements DietRecordService {

    @Resource
    private DietRecordMapper dietRecordMapper;

    @Resource
    private DietRecordItemMapper dietRecordItemMapper;

    @Resource
    private NutritionService nutritionService;

    @Resource
    private DishItemMapper dishItemMapper;

    @Override
    @Transactional
    public void addRecord(DietRecord record, List<DietRecordItem> items) {

        // 1. 保存主表
        dietRecordMapper.insert(record);
        Long recordId = record.getId();

        // 🔥 新增：统一处理后的 food 列表
        List<DietRecordItem> finalItems = new ArrayList<>();

        for (DietRecordItem item : items) {

            // =========================
            // ⭐ 如果是菜品（关键🔥）
            // =========================
            if ("dish".equals(item.getType())) {

                if (item.getDishId() == null) {
                    throw new RuntimeException("菜品ID不能为空");
                }

                List<com.maxu.maxuadmin.healthdiet.entity.DishItem> dishItems =
                        dishItemMapper.selectList(
                                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.maxu.maxuadmin.healthdiet.entity.DishItem>()
                                        .eq(com.maxu.maxuadmin.healthdiet.entity.DishItem::getDishId, item.getDishId())
                        );

                if (dishItems == null || dishItems.isEmpty()) {
                    throw new RuntimeException("菜品没有食材数据: " + item.getDishId());
                }

                // 计算菜品的原始总重量
                double originalTotalWeight = 0.0;
                for (com.maxu.maxuadmin.healthdiet.entity.DishItem di : dishItems) {
                    originalTotalWeight += di.getWeight();
                }

                // 计算比例因子：用户输入的菜品重量 / 原始总重量
                double factor = originalTotalWeight > 0 ? item.getWeight() / originalTotalWeight : 0.0;

                for (com.maxu.maxuadmin.healthdiet.entity.DishItem di : dishItems) {

                    DietRecordItem newItem = new DietRecordItem();
                    newItem.setFoodId(di.getFoodId());
                    newItem.setDishId(item.getDishId()); //记录这个食材属于哪个菜
                    newItem.setType("food"); // 标记为食材

                    // 计算食材的重量：原始重量 * 比例因子，四舍五入保留2位小数
                    double weight = di.getWeight() * factor;
                    weight = Math.round(weight * 100.0) / 100.0;
                    newItem.setWeight(weight);

                    finalItems.add(newItem);
                }

            } else {
                // =========================
                // ⭐ 普通 food
                // =========================
                item.setType("food"); // 确保 type 字段有值
                finalItems.add(item);
            }
        }

        // =========================
        // ⭐ 原有逻辑（完全复用🔥）
        // =========================
        for (DietRecordItem item : finalItems) {

            item.setRecordId(recordId);

            Nutrition nutrition = nutritionService.getByFoodId(item.getFoodId());

            if (nutrition == null) {
                throw new RuntimeException("食物没有营养数据");
            }

            double factor = item.getWeight() / 100.0;

            item.setCalorie(nutrition.getCalorie() * factor);
            item.setProtein(nutrition.getProtein() * factor);
            item.setFat(nutrition.getFat() * factor);
            item.setCarbohydrate(nutrition.getCarbohydrate() * factor);
            item.setFiber(nutrition.getFiber() != null ? nutrition.getFiber() * factor : 0.0);
            item.setSodium(nutrition.getSodium() != null ? nutrition.getSodium() * factor : 0.0);

            dietRecordItemMapper.insert(item);
        }
    }

    @Override
    public List<Map<String, Object>> getRecordList(Long userId) {
        return dietRecordMapper.getRecordList(userId);
    }

    @Override
    public List<Map<String, Object>> getRecordListByDate(Long userId, String date, String mealType) {
        List<Map<String, Object>> list = dietRecordMapper.getRecordList(userId);

        if (date != null && !date.isEmpty()) {
            list = list.stream()
                    .filter(m -> date.equals(m.get("recordDate").toString()))
                    .collect(java.util.stream.Collectors.toList());
        }

        if (mealType != null && !mealType.isEmpty()) {
            list = list.stream()
                    .filter(m -> mealType.equals(m.get("mealType").toString()))
                    .collect(java.util.stream.Collectors.toList());
        }

        // =========================
        // ⭐ 新增：按 dishId 去重（如果有 dishId，只显示菜品）
        // =========================
        Map<Long, Map<String, Object>> dishMap = new HashMap<>();
        Map<Long, Double> dishTotalWeight = new HashMap<>();
        Map<Long, Double> dishTotalCalorie = new HashMap<>();
        List<Map<String, Object>> filteredList = new ArrayList<>();
        
        for (Map<String, Object> item : list) {
            Long dishId = item.get("dishId") != null ? (Long) item.get("dishId") : null;
            
            if (dishId != null) {
                // 有 dishId，按 dishId 去重
                if (!dishMap.containsKey(dishId)) {
                    dishMap.put(dishId, item);
                    dishTotalWeight.put(dishId, 0.0);
                    dishTotalCalorie.put(dishId, 0.0);
                }
                // 累加重量和卡路里
                Double weight = item.get("weight") != null ? ((Number) item.get("weight")).doubleValue() : 0.0;
                Double calorie = item.get("calorie") != null ? ((Number) item.get("calorie")).doubleValue() : 0.0;
                dishTotalWeight.put(dishId, dishTotalWeight.get(dishId) + weight);
                dishTotalCalorie.put(dishId, dishTotalCalorie.get(dishId) + calorie);
            } else {
                // 没有 dishId，直接添加
                filteredList.add(item);
            }
        }
        
        // 添加去重后的菜品
        for (Long dishId : dishMap.keySet()) {
            Map<String, Object> dishItem = dishMap.get(dishId);
            dishItem.put("weight", dishTotalWeight.get(dishId));
            dishItem.put("calorie", dishTotalCalorie.get(dishId));
            dishItem.put("dishId", dishId); // 确保 dishId 字段存在且正确
            filteredList.add(dishItem);
        }

        Map<String, List<Map<String, Object>>> grouped = filteredList.stream()
                .collect(java.util.stream.Collectors.groupingBy(m -> m.get("recordDate") + "_" + m.get("mealType")));

        List<Map<String, Object>> result = new ArrayList<>();
        for (String key : grouped.keySet()) {
            String[] parts = key.split("_");
            List<Map<String, Object>> items = grouped.get(key);

            Map<String, Object> record = new HashMap<>();
            record.put("recordDate", parts[0]);
            record.put("mealType", parts[1]);
            record.put("totalKcal", items.stream().mapToDouble(m -> ((Number) m.get("calorie")).doubleValue()).sum());

            List<Map<String, Object>> foodItems = new ArrayList<>();
            for (Map<String, Object> item : items) {
                Map<String, Object> foodItem = new HashMap<>();
                
                // =========================
                // ⭐ 新增：优先显示 dishName（如果存在）
                // =========================
                String name = item.get("dishName") != null ? (String) item.get("dishName") : (String) item.get("foodName");
                foodItem.put("foodName", name);
                foodItem.put("weight", item.get("weight"));
                foodItem.put("calorie", item.get("calorie"));
                foodItem.put("dishId", item.get("dishId"));
                foodItems.add(foodItem);
            }
            record.put("items", foodItems);
            result.add(record);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getRecordGroup(Long userId) {

        // 查数据库 用已有的getRecordList
        List<Map<String, Object>> list = dietRecordMapper.getRecordList(userId);

        // 分组：日期 -> 餐别 -> 食物
        Map<String, Map<String, List<Map<String, Object>>>> result = new LinkedHashMap<>();

        for (Map<String, Object> row : list) {

            String date = row.get("recordDate").toString();
            String meal = row.get("mealType").toString();

            result
                    .computeIfAbsent(date, k -> new LinkedHashMap<>())
                    .computeIfAbsent(meal, k -> new ArrayList<>())
                    .add(row);
        }

        // 转换成前端需要的结构
        List<Map<String, Object>> finalList = new ArrayList<>();

        for (String date : result.keySet()) {

            Map<String, Object> dateMap = new HashMap<>();
            dateMap.put("date", date);

            List<Map<String, Object>> meals = new ArrayList<>();

            for (String meal : result.get(date).keySet()) {

                List<Map<String, Object>> foods = result.get(date).get(meal);

                // =========================
                // ⭐ 新增：按 dishId 去重（如果有 dishId，只显示菜品）
                // =========================
                Map<Long, Map<String, Object>> dishMap = new HashMap<>();
                Map<Long, Double> dishTotalWeight = new HashMap<>();
                Map<Long, Double> dishTotalCalorie = new HashMap<>();
                List<Map<String, Object>> filteredFoods = new ArrayList<>();
                
                for (Map<String, Object> item : foods) {
                    Long dishId = item.get("dishId") != null ? (Long) item.get("dishId") : null;
                    
                    if (dishId != null) {
                        // 有 dishId，按 dishId 去重
                        if (!dishMap.containsKey(dishId)) {
                            dishMap.put(dishId, item);
                            dishTotalWeight.put(dishId, 0.0);
                            dishTotalCalorie.put(dishId, 0.0);
                        }
                        // 累加重量和卡路里
                        Double weight = item.get("weight") != null ? ((Number) item.get("weight")).doubleValue() : 0.0;
                        Double calorie = item.get("calorie") != null ? ((Number) item.get("calorie")).doubleValue() : 0.0;
                        dishTotalWeight.put(dishId, dishTotalWeight.get(dishId) + weight);
                        dishTotalCalorie.put(dishId, dishTotalCalorie.get(dishId) + calorie);
                    } else {
                        // 没有 dishId，直接添加
                        filteredFoods.add(item);
                    }
                }
                
                // 添加去重后的菜品
                for (Long dishId : dishMap.keySet()) {
                    Map<String, Object> dishItem = dishMap.get(dishId);
                    dishItem.put("weight", dishTotalWeight.get(dishId));
                    dishItem.put("calorie", dishTotalCalorie.get(dishId));
                    dishItem.put("dishId", dishId); // 确保 dishId 字段存在且正确
                    filteredFoods.add(dishItem);
                }

                // 计算这一餐总热量
                double totalCal = filteredFoods.stream()
                        .mapToDouble(f -> ((Number) f.get("calorie")).doubleValue())
                        .sum();

                Map<String, Object> mealMap = new HashMap<>();
                mealMap.put("mealType", meal);
                mealMap.put("totalCalorie", totalCal);
                
                // =========================
                // ⭐ 新增：处理食物列表，优先显示菜品名称
                // =========================
                List<Map<String, Object>> processedFoods = new ArrayList<>();
                for (Map<String, Object> food : filteredFoods) {
                    Map<String, Object> processedFood = new HashMap<>(food);
                    // 优先显示 dishName（如果存在）
                    String name = food.get("dishName") != null ? (String) food.get("dishName") : (String) food.get("foodName");
                    processedFood.put("foodName", name);
                    processedFoods.add(processedFood);
                }
                
                mealMap.put("foods", processedFoods);

                meals.add(mealMap);
            }

            dateMap.put("meals", meals);
            finalList.add(dateMap);
        }

        return finalList;
    }

    @Override
    public Map<String, Object> getStat(Long userId) {
        return dietRecordMapper.getStat(userId);
    }

    @Override
    public Map<String, Object> getTrendStat(Long userId, String type) {
        return dietRecordMapper.getTrendStat(userId, type);
    }

    @Override
    public List<Map<String, Object>> getTrendLine(Long userId) {
        return dietRecordMapper.getTrendLine(userId);
    }


    @Resource
    private UserMapper userMapper;
    @Override
    public Map<String, Object> getTodayCalorieWithTarget(Long userId) {

        // 今日摄入
        Map<String, Object> stat = dietRecordMapper.getStat(userId);
        double intake = ((Number) stat.get("calorie")).doubleValue();

        // 查用户
        User user = userMapper.selectById(userId);

        double weight = user.getWeight();
        double height = user.getHeight();
        int age = user.getAge();
        int gender = user.getGender(); // 1男 0女
        Map<String, Double> activityMap = new HashMap<>();
        activityMap.put("久坐", 1.2);
        activityMap.put("轻度活动", 1.375);
        activityMap.put("中度活动", 1.55);
        activityMap.put("高度活动", 1.725);
        activityMap.put("高强度", 1.9);
        double activity = activityMap.getOrDefault(user.getActivityLevel(), 1.2);

        // 计算BMR
        double bmr;
        if (gender == 1) {
            bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        }

        // 计算TDEE
        double tdee = bmr * activity;

        // 根据目标调整
        String goal = user.getHealthGoal();
        if ("减脂".equals(goal)) {
            tdee *= 0.8;
        } else if ("增肌".equals(goal)) {
            tdee *= 1.2;
        }

        // 返回
        Map<String, Object> result = new HashMap<>();
        result.put("intake", (int) intake);
        result.put("target", (int) tdee);

        return result;
    }

    @Override
    @Transactional
    public void updateRecord(Long userId, String date, String mealType, DietRecordDTO dto) {
        List<DietRecord> existingRecords = dietRecordMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<DietRecord>()
                        .eq(DietRecord::getUserId, userId)
                        .eq(DietRecord::getRecordDate, date)
                        .eq(DietRecord::getMealType, mealType)
        );

        for (DietRecord record : existingRecords) {
            dietRecordItemMapper.delete(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<DietRecordItem>()
                            .eq(DietRecordItem::getRecordId, record.getId())
            );
            dietRecordMapper.deleteById(record.getId());
        }

        DietRecord record = dto.getRecord();
        dietRecordMapper.insert(record);
        Long recordId = record.getId();

        List<DietRecordItem> finalItems = new ArrayList<>();

        for (DietRecordDTO.Item i : dto.getItems()) {
            DietRecordItem item = new DietRecordItem();
            item.setFoodId(i.getFoodId());
            item.setDishId(i.getDishId());
            item.setWeight(i.getWeight());
            item.setType(i.getType());

            if ("dish".equals(item.getType())) {
                if (item.getDishId() == null) {
                    throw new RuntimeException("菜品ID不能为空");
                }

                List<com.maxu.maxuadmin.healthdiet.entity.DishItem> dishItems =
                        dishItemMapper.selectList(
                                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.maxu.maxuadmin.healthdiet.entity.DishItem>()
                                        .eq(com.maxu.maxuadmin.healthdiet.entity.DishItem::getDishId, item.getDishId())
                        );

                if (dishItems == null || dishItems.isEmpty()) {
                    throw new RuntimeException("菜品没有食材数据: " + item.getDishId());
                }

                double originalTotalWeight = 0.0;
                for (com.maxu.maxuadmin.healthdiet.entity.DishItem di : dishItems) {
                    originalTotalWeight += di.getWeight();
                }

                double factor = originalTotalWeight > 0 ? item.getWeight() / originalTotalWeight : 0.0;

                for (com.maxu.maxuadmin.healthdiet.entity.DishItem di : dishItems) {
                    DietRecordItem newItem = new DietRecordItem();
                    newItem.setFoodId(di.getFoodId());
                    newItem.setDishId(item.getDishId());
                    newItem.setType("food");
                    newItem.setWeight(di.getWeight() * factor);
                    finalItems.add(newItem);
                }
            } else {
                item.setType("food");
                finalItems.add(item);
            }
        }

        for (DietRecordItem item : finalItems) {
            item.setRecordId(recordId);

            Nutrition nutrition = nutritionService.getByFoodId(item.getFoodId());

            if (nutrition == null) {
                throw new RuntimeException("食物没有营养数据");
            }

            double factor = item.getWeight() / 100.0;

            item.setCalorie(nutrition.getCalorie() * factor);
            item.setProtein(nutrition.getProtein() * factor);
            item.setFat(nutrition.getFat() * factor);
            item.setCarbohydrate(nutrition.getCarbohydrate() * factor);
            item.setFiber(nutrition.getFiber() != null ? nutrition.getFiber() * factor : 0.0);
            item.setSodium(nutrition.getSodium() != null ? nutrition.getSodium() * factor : 0.0);

            dietRecordItemMapper.insert(item);
        }
    }

    @Override
    @Transactional
    public void deleteRecord(Long userId, String date, String mealType) {
        List<DietRecord> existingRecords = dietRecordMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<DietRecord>()
                        .eq(DietRecord::getUserId, userId)
                        .eq(DietRecord::getRecordDate, date)
                        .eq(DietRecord::getMealType, mealType)
        );

        for (DietRecord record : existingRecords) {
            dietRecordItemMapper.delete(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<DietRecordItem>()
                            .eq(DietRecordItem::getRecordId, record.getId())
            );
            dietRecordMapper.deleteById(record.getId());
        }
    }

    @Resource
    private com.maxu.maxuadmin.healthdiet.mapper.FoodMapper foodMapper;
    @Resource
    private com.maxu.maxuadmin.healthdiet.mapper.DishMapper dishMapper;

    @Override
    public com.maxu.maxuadmin.healthdiet.dto.UserNutritionDTO getUserNutrition(Long userId) {
        com.maxu.maxuadmin.healthdiet.dto.UserNutritionDTO dto = new com.maxu.maxuadmin.healthdiet.dto.UserNutritionDTO();

        List<com.maxu.maxuadmin.healthdiet.dto.UserNutritionDTO.DietRecordItemVO> records = new ArrayList<>();

        double totalCalorie = 0;
        double totalProtein = 0;
        double totalFat = 0;
        double totalCarbohydrate = 0;
        double totalFiber = 0;
        double totalSodium = 0;
        Set<Long> foodIds = new HashSet<>();

        List<DietRecord> dietRecords = dietRecordMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<DietRecord>()
                        .eq(DietRecord::getUserId, userId)
                        .orderByDesc(DietRecord::getRecordDate)
                        .last("LIMIT 30")
        );

        Map<String, com.maxu.maxuadmin.healthdiet.dto.UserNutritionDTO.DietRecordItemVO> uniqueMap = new LinkedHashMap<>();

        for (DietRecord record : dietRecords) {
            List<DietRecordItem> items = dietRecordItemMapper.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<DietRecordItem>()
                            .eq(DietRecordItem::getRecordId, record.getId())
            );

            for (DietRecordItem item : items) {
                String dateStr = record.getRecordDate() != null ? record.getRecordDate().toString() : "";
                String mealType = record.getMealType();
                String key;

                if (item.getDishId() != null) {
                    key = dateStr + "_" + mealType + "_dish_" + item.getDishId();
                } else if (item.getFoodId() != null) {
                    key = dateStr + "_" + mealType + "_food_" + item.getFoodId();
                } else {
                    continue;
                }

                com.maxu.maxuadmin.healthdiet.dto.UserNutritionDTO.DietRecordItemVO existingItem = uniqueMap.get(key);
                if (existingItem != null) {
                    existingItem.setWeight(existingItem.getWeight() + (item.getWeight() != null ? item.getWeight() : 0));
                    existingItem.setCalorie(existingItem.getCalorie() + (item.getCalorie() != null ? item.getCalorie() : 0));
                    existingItem.setProtein(existingItem.getProtein() + (item.getProtein() != null ? item.getProtein() : 0));
                    existingItem.setFat(existingItem.getFat() + (item.getFat() != null ? item.getFat() : 0));
                    existingItem.setCarbohydrate(existingItem.getCarbohydrate() + (item.getCarbohydrate() != null ? item.getCarbohydrate() : 0));
                    existingItem.setFiber(existingItem.getFiber() + (item.getFiber() != null ? item.getFiber() : 0));
                    existingItem.setSodium(existingItem.getSodium() + (item.getSodium() != null ? item.getSodium() : 0));
                } else {
                    com.maxu.maxuadmin.healthdiet.dto.UserNutritionDTO.DietRecordItemVO itemVO =
                            new com.maxu.maxuadmin.healthdiet.dto.UserNutritionDTO.DietRecordItemVO();
                    itemVO.setId(item.getId());
                    itemVO.setMealType(mealType);
                    itemVO.setRecordDate(dateStr);
                    itemVO.setType(item.getType());
                    itemVO.setWeight(item.getWeight());
                    itemVO.setCalorie(item.getCalorie());
                    itemVO.setProtein(item.getProtein());
                    itemVO.setFat(item.getFat());
                    itemVO.setCarbohydrate(item.getCarbohydrate());
                    itemVO.setFiber(item.getFiber());
                    itemVO.setSodium(item.getSodium());

                    String foodName = "";
                    if (item.getDishId() != null) {
                        com.maxu.maxuadmin.healthdiet.entity.Dish dish = dishMapper.selectById(item.getDishId());
                        if (dish != null) {
                            foodName = dish.getName();
                            itemVO.setDishId(item.getDishId());
                        }
                    } else if (item.getFoodId() != null) {
                        com.maxu.maxuadmin.healthdiet.entity.Food food = foodMapper.selectById(item.getFoodId());
                        if (food != null) {
                            foodName = food.getName();
                            foodIds.add(item.getFoodId());
                        }
                    }
                    itemVO.setFoodName(foodName);

                    uniqueMap.put(key, itemVO);
                }
            }
        }

        records.addAll(uniqueMap.values());

        for (com.maxu.maxuadmin.healthdiet.dto.UserNutritionDTO.DietRecordItemVO item : records) {
            totalCalorie += item.getCalorie() != null ? item.getCalorie() : 0;
            totalProtein += item.getProtein() != null ? item.getProtein() : 0;
            totalFat += item.getFat() != null ? item.getFat() : 0;
            totalCarbohydrate += item.getCarbohydrate() != null ? item.getCarbohydrate() : 0;
            totalFiber += item.getFiber() != null ? item.getFiber() : 0;
            totalSodium += item.getSodium() != null ? item.getSodium() : 0;
        }

        com.maxu.maxuadmin.healthdiet.dto.UserNutritionDTO.NutritionSummary summary =
                new com.maxu.maxuadmin.healthdiet.dto.UserNutritionDTO.NutritionSummary();
        summary.setTotalCalorie(Math.round(totalCalorie * 10) / 10.0);
        summary.setTotalProtein(Math.round(totalProtein * 10) / 10.0);
        summary.setTotalFat(Math.round(totalFat * 10) / 10.0);
        summary.setTotalCarbohydrate(Math.round(totalCarbohydrate * 10) / 10.0);
        summary.setTotalFiber(Math.round(totalFiber * 10) / 10.0);
        summary.setTotalSodium(Math.round(totalSodium * 10) / 10.0);
        summary.setRecordCount(dietRecords.size());
        summary.setFoodCount(foodIds.size());

        dto.setRecords(records);
        dto.setSummary(summary);

        return dto;
    }

    @Override
    public List<com.maxu.maxuadmin.healthdiet.entity.DietRecord> listByUser(Long userId) {
        return dietRecordMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<DietRecord>()
                        .eq(DietRecord::getUserId, userId)
                        .orderByDesc(DietRecord::getRecordDate)
        );
    }
}
