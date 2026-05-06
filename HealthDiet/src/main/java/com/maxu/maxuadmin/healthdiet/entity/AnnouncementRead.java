package com.maxu.maxuadmin.healthdiet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("announcement_read")
public class AnnouncementRead {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long announcementId;
    private Integer isRead;
    private LocalDateTime readTime;
}
