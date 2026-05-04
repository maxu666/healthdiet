package com.maxu.maxuadmin.healthdiet.dto;

import com.maxu.maxuadmin.healthdiet.entity.DietRecord;
import lombok.Data;

import java.util.List;

@Data
public class DietRecordDTO {

    // 一顿饭的信息
    private DietRecord record;

    // 吃的食物列表
    private List<Item> items;

    @Data
    public static class Item {
        private Long foodId;
        private Long dishId;
        private Double weight;
        private String type;
    }
}
