package com.maxu.maxuadmin.healthdiet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxu.maxuadmin.healthdiet.entity.Announcement;
import com.maxu.maxuadmin.healthdiet.entity.AnnouncementRead;
import com.maxu.maxuadmin.healthdiet.mapper.AnnouncementMapper;
import com.maxu.maxuadmin.healthdiet.mapper.AnnouncementReadMapper;
import com.maxu.maxuadmin.healthdiet.vo.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/message")
@CrossOrigin
public class MessageController {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private AnnouncementReadMapper announcementReadMapper;

    @GetMapping("/unread/count")
    public Map<String, Object> getUnreadCount(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        int announcementUnread = getAnnouncementUnreadCount(userId);
        result.put("success", true);
        result.put("total", announcementUnread);
        return result;
    }

    @GetMapping("/list")
    public Map<String, Object> getMessageList(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        List<MessageVO> messages = getAnnouncementMessages(userId);
        messages.sort((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()));
        result.put("success", true);
        result.put("data", messages);
        return result;
    }

    @PostMapping("/read/announcement")
    public Map<String, Object> markAnnouncementRead(@RequestParam Long userId, @RequestParam Long announcementId) {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<AnnouncementRead> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AnnouncementRead::getUserId, userId)
               .eq(AnnouncementRead::getAnnouncementId, announcementId);
        AnnouncementRead existing = announcementReadMapper.selectOne(wrapper);
        if (existing == null) {
            AnnouncementRead ar = new AnnouncementRead();
            ar.setUserId(userId);
            ar.setAnnouncementId(announcementId);
            ar.setIsRead(1);
            ar.setReadTime(LocalDateTime.now());
            announcementReadMapper.insert(ar);
        } else {
            existing.setIsRead(1);
            existing.setReadTime(LocalDateTime.now());
            announcementReadMapper.updateById(existing);
        }
        result.put("success", true);
        return result;
    }

    @PostMapping("/read/all")
    public Map<String, Object> markAllRead(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<Announcement> announcementWrapper = new LambdaQueryWrapper<>();
        announcementWrapper.eq(Announcement::getStatus, 1);
        List<Announcement> announcements = announcementMapper.selectList(announcementWrapper);
        for (Announcement announcement : announcements) {
            LambdaQueryWrapper<AnnouncementRead> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AnnouncementRead::getUserId, userId)
                   .eq(AnnouncementRead::getAnnouncementId, announcement.getId());
            AnnouncementRead existing = announcementReadMapper.selectOne(wrapper);
            if (existing == null) {
                AnnouncementRead ar = new AnnouncementRead();
                ar.setUserId(userId);
                ar.setAnnouncementId(announcement.getId());
                ar.setIsRead(1);
                ar.setReadTime(LocalDateTime.now());
                announcementReadMapper.insert(ar);
            }
        }
        result.put("success", true);
        return result;
    }

    private int getAnnouncementUnreadCount(Long userId) {
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Announcement::getStatus, 1);
        List<Announcement> announcements = announcementMapper.selectList(wrapper);
        int unreadCount = 0;
        for (Announcement announcement : announcements) {
            LambdaQueryWrapper<AnnouncementRead> readWrapper = new LambdaQueryWrapper<>();
            readWrapper.eq(AnnouncementRead::getUserId, userId)
                      .eq(AnnouncementRead::getAnnouncementId, announcement.getId())
                      .eq(AnnouncementRead::getIsRead, 1);
            AnnouncementRead read = announcementReadMapper.selectOne(readWrapper);
            if (read == null) {
                unreadCount++;
            }
        }
        return unreadCount;
    }

    private List<MessageVO> getAnnouncementMessages(Long userId) {
        List<MessageVO> messages = new ArrayList<>();
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Announcement::getStatus, 1)
               .orderByDesc(Announcement::getCreateTime);
        List<Announcement> announcements = announcementMapper.selectList(wrapper);
        for (Announcement announcement : announcements) {
            LambdaQueryWrapper<AnnouncementRead> readWrapper = new LambdaQueryWrapper<>();
            readWrapper.eq(AnnouncementRead::getUserId, userId)
                      .eq(AnnouncementRead::getAnnouncementId, announcement.getId())
                      .eq(AnnouncementRead::getIsRead, 1);
            AnnouncementRead read = announcementReadMapper.selectOne(readWrapper);
            MessageVO vo = new MessageVO();
            vo.setId(announcement.getId());
            vo.setType("ANNOUNCEMENT");
            vo.setTitle(announcement.getTitle());
            vo.setContent(announcement.getContent());
            vo.setCreateTime(announcement.getCreateTime());
            vo.setIsRead(read != null);
            messages.add(vo);
        }
        return messages;
    }
}
