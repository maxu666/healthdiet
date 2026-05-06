package com.maxu.maxuadmin.healthdiet.controller;

import com.maxu.maxuadmin.healthdiet.entity.HealthNews;
import com.maxu.maxuadmin.healthdiet.service.HealthNewsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/healthNews")
@CrossOrigin
public class HealthNewsController {

    @Resource
    private HealthNewsService healthNewsService;

    @GetMapping("/list")
    public List<HealthNews> list() {
        return healthNewsService.list();
    }

    @GetMapping("/{id}")
    public HealthNews getById(@PathVariable Long id) {
        return healthNewsService.getById(id);
    }

    @PostMapping("/add")
    public boolean add(@RequestBody HealthNews healthNews) {
        healthNews.setCreateTime(LocalDateTime.now());
        if (healthNews.getStatus() == null) {
            healthNews.setStatus(1);
        }
        return healthNewsService.save(healthNews);
    }

    @PostMapping("/update")
    public boolean update(@RequestBody HealthNews healthNews) {
        return healthNewsService.updateById(healthNews);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return healthNewsService.removeById(id);
    }
}