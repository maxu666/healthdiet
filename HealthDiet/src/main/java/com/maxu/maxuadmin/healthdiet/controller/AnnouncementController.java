package com.maxu.maxuadmin.healthdiet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxu.maxuadmin.healthdiet.entity.Announcement;
import com.maxu.maxuadmin.healthdiet.service.AnnouncementService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/annoucement")
@CrossOrigin
public class AnnouncementController {
    @Resource
    private AnnouncementService annoucementService;

    // 查询所有（按创建时间倒序）
    @GetMapping("/list")
    public List<Announcement> list() {
        LambdaQueryWrapper<Announcement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Announcement::getCreateTime);
        return annoucementService.list(queryWrapper);
    }

    // 根据ID查询
    @GetMapping("/{id}")
    public Announcement getById(@PathVariable Long id) {
        return annoucementService.getById(id);
    }

    // 新增
    @PostMapping("/add")
    public boolean add(@RequestBody Announcement announcement) {
        announcement.setCreateTime(LocalDateTime.now());
        if (announcement.getStatus() == null) {
            announcement.setStatus(1); // 1-已发布
        }
        return annoucementService.save(announcement);
    }

    // 修改
    @PostMapping("/update")
    public boolean update(@RequestBody Announcement announcement) {
        return annoucementService.updateById(announcement);
    }

    // 删除
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return annoucementService.removeById(id);
    }
}
