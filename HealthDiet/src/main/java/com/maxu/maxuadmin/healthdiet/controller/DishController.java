package com.maxu.maxuadmin.healthdiet.controller;

import com.maxu.maxuadmin.healthdiet.entity.Dish;
import com.maxu.maxuadmin.healthdiet.entity.DishItem;
import com.maxu.maxuadmin.healthdiet.service.DishService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/dish")
public class DishController {

    @Resource
    private DishService dishService;

    /**
     * ✅ 新增菜品（菜品 + 食材）
     */
    @PostMapping("/addFull")
    public String addFull(@RequestBody Map<String, Object> data) {

        // 1️⃣ 取 dish
        Dish dish = convertDish(data.get("dish"));

        // 2️⃣ 取 items
        List<DishItem> items = convertItems(data.get("items"));

        // 3️⃣ 调用 service
        dishService.addFull(dish, items);

        return "新增成功";
    }

    /**
     * ✅ 查询所有菜品
     */
    @GetMapping("/list")
    public List<Dish> list() {
        return dishService.list();
    }

    /**
     * ✅ 查询菜品详情
     */
    @GetMapping("/detail")
    public Map<String, Object> detail(@RequestParam Long id) {
        return dishService.getDetail(id);
    }

    /**
     * ✅ 删除菜品
     */
    @DeleteMapping("/delete")
    public String delete(@RequestParam Long id) {
        dishService.deleteById(id);
        return "删除成功";
    }

    /**
     * ✅ 根据创建者ID查询菜品
     */
    @GetMapping("/listByCreator")
    public List<Dish> listByCreator(@RequestParam Long creatorId) {
        return dishService.listByCreator(creatorId);
    }

    /**
     * ✅ 更新菜品（菜品 + 食材）
     */
    @PutMapping("/update")
    public String update(@RequestBody Map<String, Object> data) {

        Dish dish = convertDish(data.get("dish"));
        List<DishItem> items = convertItems(data.get("items"));

        dishService.updateFull(dish, items);

        return "更新成功";
    }

    // ================= 工具方法 =================

    private Dish convertDish(Object obj) {
        Map<String, Object> map = (Map<String, Object>) obj;

        Dish dish = new Dish();
        dish.setId(map.get("id") == null ? null : Long.valueOf(map.get("id").toString()));
        dish.setName((String) map.get("name"));
        dish.setCategory((String) map.get("category"));
        dish.setImageUrl((String) map.get("imageUrl"));
        dish.setDescription((String) map.get("description"));
        dish.setCreatorId(map.get("creatorId") == null ? null : Long.valueOf(map.get("creatorId").toString()));
        dish.setCreatorRole((String) map.get("creatorRole"));

        return dish;
    }

    private List<DishItem> convertItems(Object obj) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) obj;

        return list.stream().map(m -> {
            DishItem item = new DishItem();
            item.setFoodId(Long.valueOf(m.get("foodId").toString()));
            item.setWeight(Double.valueOf(m.get("weight").toString()));
            return item;
        }).toList();
    }
}