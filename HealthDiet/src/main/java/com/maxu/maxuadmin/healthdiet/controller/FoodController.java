package com.maxu.maxuadmin.healthdiet.controller;

import com.maxu.maxuadmin.healthdiet.dto.FoodDTO;
import com.maxu.maxuadmin.healthdiet.entity.Food;
import com.maxu.maxuadmin.healthdiet.service.FoodService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/food")
public class FoodController {

    @Resource
    private FoodService foodService;

    //添加食物
    @PostMapping("/addFull")
    public String addFull(@RequestBody FoodDTO dto) {
        foodService.addFull(dto.getFood(), dto.getNutrition());
        return "新增成功";
    }

    //修改
    @PutMapping("/updateFull")
    public String updateFull(@RequestBody FoodDTO dto) {
        foodService.updateFull(dto.getFood(), dto.getNutrition());
        return "修改成功";
    }

    //删除
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        foodService.removeById(id);
        return "删除成功";
    }

    //查询所有食物（包含营养信息）
    @GetMapping("/listFull")
    public List<FoodDTO> listFull() {
        return foodService.listFull();
    }

    //查询所有食物
    @GetMapping("/list")
    public List<Food> list() {
        return foodService.list();
    }

    //根据ID查询（包含营养信息）
    @GetMapping("/{id}")
    public FoodDTO getById(@PathVariable Long id) {
        return foodService.getFullById(id);
    }
}
