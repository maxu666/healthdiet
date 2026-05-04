package com.maxu.maxuadmin.healthdiet.service;

import com.maxu.maxuadmin.healthdiet.dto.ChatMessageDTO;
import com.maxu.maxuadmin.healthdiet.vo.ChatMessageVO;

import java.util.List;

public interface ChatMessageService {

    void sendMessage(ChatMessageDTO dto);

    //获取会话消息
    List<ChatMessageVO> listBySession(Long sessionId);

    //标记消息已读
    void markAsRead(Long sessionId, Long userId);
}
