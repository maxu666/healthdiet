package com.maxu.maxuadmin.healthdiet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.maxu.maxuadmin.healthdiet.dto.ChatMessageDTO;
import com.maxu.maxuadmin.healthdiet.entity.ChatMessage;
import com.maxu.maxuadmin.healthdiet.mapper.ChatMessageMapper;
import com.maxu.maxuadmin.healthdiet.service.ChatMessageService;
import com.maxu.maxuadmin.healthdiet.service.ChatSessionService;
import com.maxu.maxuadmin.healthdiet.vo.ChatMessageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    @Lazy
    private ChatSessionService chatSessionService;

    @Override
    public void sendMessage(ChatMessageDTO dto) {
        ChatMessage message = new ChatMessage();
        message.setSessionId(dto.getSessionId());
        message.setSenderId(dto.getSenderId());
        message.setSenderRole(dto.getSenderRole());
        message.setContent(dto.getContent());
        message.setIsRead(0);
        message.setCreateTime(LocalDateTime.now());

        chatMessageMapper.insert(message);
        chatSessionService.updateLastMessageTime(dto.getSessionId());
    }

    @Override
    public List<ChatMessageVO> listBySession(Long sessionId) {
        List<ChatMessage> list = chatMessageMapper.selectList(
                new QueryWrapper<ChatMessage>()
                        .eq("session_id", sessionId)
                        .orderByAsc("create_time")
        );

        return list.stream().map(this::convertToVO).toList();
    }

    @Override
    public void markAsRead(Long sessionId, Long userId) {
        ChatMessage update = new ChatMessage();
        update.setIsRead(1);

        chatMessageMapper.update(
                update,
                new UpdateWrapper<ChatMessage>()
                        .eq("session_id", sessionId)
                        .ne("sender_id", userId)
                        .eq("is_read", 0)
        );
    }

    private ChatMessageVO convertToVO(ChatMessage msg) {
        ChatMessageVO vo = new ChatMessageVO();
        BeanUtils.copyProperties(msg, vo);
        return vo;
    }
}