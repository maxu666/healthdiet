package com.maxu.maxuadmin.healthdiet.controller;

import com.maxu.maxuadmin.healthdiet.dto.ChatSessionDTO;
import com.maxu.maxuadmin.healthdiet.service.ChatSessionService;
import com.maxu.maxuadmin.healthdiet.vo.ChatSessionVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatSession")
@CrossOrigin
public class ChatSessionController {

    @Resource
    private ChatSessionService chatSessionService;

    @PostMapping("/create")
    public Long create(@RequestBody ChatSessionDTO dto) {
        return chatSessionService.createSession(dto);
    }

    @GetMapping("/list/user/{userId}")
    public List<ChatSessionVO> listByUser(@PathVariable Long userId) {
        return chatSessionService.listByUser(userId);
    }

    @GetMapping("/list/nutritionist/{nutritionistId}")
    public List<ChatSessionVO> listByNutritionist(@PathVariable Long nutritionistId) {
        return chatSessionService.listByNutritionist(nutritionistId);
    }

    @GetMapping("/detail/{id}")
    public ChatSessionVO detail(@PathVariable Long id) {
        return chatSessionService.getById(id);
    }

    @PutMapping("/updateTime/{sessionId}")
    public void updateLastMessageTime(@PathVariable Long sessionId) {
        chatSessionService.updateLastMessageTime(sessionId);
    }

    @GetMapping("/getOrCreate")
    public ChatSessionVO getOrCreate(
            @RequestParam Long userId,
            @RequestParam Long nutritionistId,
            @RequestParam(required = false) String title) {
        return chatSessionService.getOrCreateSession(userId, nutritionistId, title);
    }
}
