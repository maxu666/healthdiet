package com.maxu.maxuadmin.healthdiet.controller;

import com.maxu.maxuadmin.healthdiet.service.AiService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@CrossOrigin
public class AiController {

    @Resource
    private AiService aiService;

    @PostMapping("/ask")
    public Map<String, Object> ask(@RequestBody String question) {

        String answer = aiService.ask(question);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", answer);

        return result;
    }
}
