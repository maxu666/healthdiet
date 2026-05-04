package com.maxu.maxuadmin.healthdiet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxu.maxuadmin.healthdiet.entity.Announcement;
import com.maxu.maxuadmin.healthdiet.mapper.AnnouncementMapper;
import com.maxu.maxuadmin.healthdiet.service.AnnouncementService;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService{
}
