package com.maxu.maxuadmin.healthdiet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maxu.maxuadmin.healthdiet.dto.ChatSessionDTO;
import com.maxu.maxuadmin.healthdiet.entity.ChatSession;
import com.maxu.maxuadmin.healthdiet.entity.User;
import com.maxu.maxuadmin.healthdiet.mapper.ChatSessionMapper;
import com.maxu.maxuadmin.healthdiet.mapper.UserMapper;
import com.maxu.maxuadmin.healthdiet.service.ChatSessionService;
import com.maxu.maxuadmin.healthdiet.vo.ChatSessionVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatSessionServiceImpl implements ChatSessionService {

    @Resource
    private ChatSessionMapper chatSessionMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public Long createSession(ChatSessionDTO dto) {
        ChatSession session = new ChatSession();
        session.setUserId(dto.getUserId());
        session.setNutritionistId(dto.getNutritionistId());
        session.setTitle(dto.getTitle());
        session.setStatus(0);

        LocalDateTime now = LocalDateTime.now();
        session.setCreateTime(now);
        session.setLastMessageTime(now);

        chatSessionMapper.insert(session);
        return session.getId();
    }

    @Override
    public List<ChatSessionVO> listByUser(Long userId) {
        List<ChatSession> list = chatSessionMapper.selectList(
                new QueryWrapper<ChatSession>()
                        .eq("user_id", userId)
                        .orderByDesc("last_message_time")
        );

        return list.stream().map(this::convertToVOForUser).toList();
    }

    @Override
    public List<ChatSessionVO> listByNutritionist(Long nutritionistId) {
        List<ChatSession> list = chatSessionMapper.selectList(
                new QueryWrapper<ChatSession>()
                        .eq("nutritionist_id", nutritionistId)
                        .orderByDesc("last_message_time")
        );

        return list.stream().map(this::convertToVOForNutritionist).toList();
    }

    @Override
    public ChatSessionVO getById(Long id) {
        ChatSession session = chatSessionMapper.selectById(id);
        if (session == null) {
            return null;
        }
        return convertToVO(session);
    }

    @Override
    public void updateLastMessageTime(Long sessionId) {
        ChatSession session = new ChatSession();
        session.setId(sessionId);
        session.setLastMessageTime(LocalDateTime.now());
        chatSessionMapper.updateById(session);
    }

    private ChatSessionVO convertToVO(ChatSession session) {
        ChatSessionVO vo = new ChatSessionVO();
        BeanUtils.copyProperties(session, vo);
        return vo;
    }

    private ChatSessionVO convertToVOForUser(ChatSession session) {
        ChatSessionVO vo = new ChatSessionVO();
        BeanUtils.copyProperties(session, vo);
        
        // 获取营养师信息
        User nutritionist = userMapper.selectById(session.getNutritionistId());
        if (nutritionist != null) {
            String nickname = nutritionist.getNickname();
            if (nickname == null || nickname.isEmpty()) {
                nickname = nutritionist.getUsername();
            }
            vo.setTitle("与 " + nickname + " 的咨询");
        }
        
        return vo;
    }

    private ChatSessionVO convertToVOForNutritionist(ChatSession session) {
        ChatSessionVO vo = new ChatSessionVO();
        BeanUtils.copyProperties(session, vo);
        
        // 获取用户信息
        User user = userMapper.selectById(session.getUserId());
        if (user != null) {
            String nickname = user.getNickname();
            if (nickname == null || nickname.isEmpty()) {
                nickname = user.getUsername();
            }
            vo.setTitle("与 " + nickname + " 的咨询");
        }
        
        return vo;
    }

    @Override
    public ChatSessionVO getOrCreateSession(Long userId, Long nutritionistId, String title) {
        QueryWrapper<ChatSession> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("nutritionist_id", nutritionistId);
        wrapper.orderByDesc("create_time");
        wrapper.last("LIMIT 1");
        
        ChatSession session = chatSessionMapper.selectOne(wrapper);
        
        if (session != null) {
            return convertToVO(session);
        }
        
        ChatSession newSession = new ChatSession();
        newSession.setUserId(userId);
        newSession.setNutritionistId(nutritionistId);
        newSession.setTitle(title);
        newSession.setStatus(0);
        
        LocalDateTime now = LocalDateTime.now();
        newSession.setCreateTime(now);
        newSession.setLastMessageTime(now);
        
        chatSessionMapper.insert(newSession);
        return convertToVO(newSession);
    }
}