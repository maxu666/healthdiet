package com.maxu.maxuadmin.healthdiet.controller;

import com.maxu.maxuadmin.healthdiet.dto.ChatMessageDTO;
import com.maxu.maxuadmin.healthdiet.service.ChatMessageService;
import com.maxu.maxuadmin.healthdiet.vo.ChatMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatMessage")
@CrossOrigin
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    @PostMapping("/send")
    public void sendMessage(@RequestBody ChatMessageDTO dto) {
        chatMessageService.sendMessage(dto);
    }

    @GetMapping("/list/{sessionId}")
    public List<ChatMessageVO> listBySession(@PathVariable Long sessionId) {
        return chatMessageService.listBySession(sessionId);
    }

    @PutMapping("/read/{sessionId}/{userId}")
    public void markAsRead(@PathVariable Long sessionId, @PathVariable Long userId) {
        chatMessageService.markAsRead(sessionId, userId);
    }
}