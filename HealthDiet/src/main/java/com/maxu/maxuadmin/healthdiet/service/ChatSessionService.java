package com.maxu.maxuadmin.healthdiet.service;

import com.maxu.maxuadmin.healthdiet.dto.ChatSessionDTO;
import com.maxu.maxuadmin.healthdiet.vo.ChatSessionVO;

import java.util.List;

public interface ChatSessionService {
    Long createSession(ChatSessionDTO dto);

    List<ChatSessionVO> listByUser(Long userId);

    List<ChatSessionVO> listByNutritionist(Long nutritionistId);

    ChatSessionVO getById(Long id);

    void updateLastMessageTime(Long sessionId);

    ChatSessionVO getOrCreateSession(Long userId, Long nutritionistId, String title);
}
