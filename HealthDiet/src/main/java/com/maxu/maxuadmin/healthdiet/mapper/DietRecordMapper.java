package com.maxu.maxuadmin.healthdiet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maxu.maxuadmin.healthdiet.entity.DietRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DietRecordMapper extends BaseMapper<DietRecord> {
    @Select("""
SELECT 
  dr.id AS recordId,
  dr.record_date AS recordDate,
  dr.meal_type AS mealType,

  dri.food_id AS foodId,
  dri.dish_id AS dishId,
  dri.weight,
  dri.calorie,

  f.name AS foodName,
  d.name AS dishName

FROM diet_record dr
JOIN diet_record_item dri ON dr.id = dri.record_id
JOIN food f ON dri.food_id = f.id
LEFT JOIN dish d ON dri.dish_id = d.id

WHERE dr.user_id = #{userId}

ORDER BY dr.record_date DESC, dr.meal_type
""")
    List<Map<String, Object>> getRecordList(Long userId);

    // ✅ 首页统计（今日）
    @Select("""
        SELECT 
          IFNULL(SUM(dri.carbohydrate), 0) AS carbohydrate,
          IFNULL(SUM(dri.protein), 0) AS protein,
          IFNULL(SUM(dri.fat), 0) AS fat,
          IFNULL(SUM(dri.calorie), 0) AS calorie
        FROM diet_record dr
        JOIN diet_record_item dri ON dr.id = dri.record_id
        WHERE dr.user_id = #{userId}
          AND DATE(dr.record_date) = CURDATE()
    """)
    Map<String, Object> getStat(Long userId);

    @Select("""
SELECT 
  IFNULL(SUM(dri.carbohydrate), 0) AS carbohydrate,
  IFNULL(SUM(dri.protein), 0) AS protein,
  IFNULL(SUM(dri.fat), 0) AS fat
FROM diet_record dr
JOIN diet_record_item dri ON dr.id = dri.record_id
WHERE dr.user_id = #{userId}
  AND (
    (#{type} = 'day' AND DATE(dr.record_date) = CURDATE())
    OR
    (#{type} = 'week' AND YEARWEEK(dr.record_date) = YEARWEEK(CURDATE()))
    OR
    (#{type} = 'month' AND DATE_FORMAT(dr.record_date, '%Y-%m') = DATE_FORMAT(CURDATE(), '%Y-%m'))
  )
""")
    Map<String, Object> getTrendStat(Long userId, String type);

    @Select("""
SELECT 
  DATE(dr.record_date) AS date,
  IFNULL(SUM(dri.calorie), 0) AS calorie
FROM diet_record dr
JOIN diet_record_item dri ON dr.id = dri.record_id
WHERE dr.user_id = #{userId}
GROUP BY DATE(dr.record_date)
ORDER BY date
""")
    List<Map<String, Object>> getTrendLine(Long userId);

    @Select("""
SELECT 
  dr.id AS recordId,
  dr.record_date AS recordDate,
  dr.meal_type AS mealType,
  dri.food_id AS foodId,
  dri.dish_id AS dishId,
  dri.weight,
  dri.calorie,
  f.name AS foodName,
  d.name AS dishName
FROM diet_record dr
JOIN diet_record_item dri ON dr.id = dri.record_id
JOIN food f ON dri.food_id = f.id
LEFT JOIN dish d ON dri.dish_id = d.id
WHERE dr.user_id = #{userId}
  AND DATE(dr.record_date) = #{date}
  AND dr.meal_type = #{mealType}
""")
    List<Map<String, Object>> getRecordByDateAndMeal(Long userId, String date, String mealType);
}
