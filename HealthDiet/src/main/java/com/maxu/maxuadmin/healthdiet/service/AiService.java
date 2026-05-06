package com.maxu.maxuadmin.healthdiet.service;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class AiService {

    private static final Logger log = LoggerFactory.getLogger(AiService.class);

    @Value("${ai.api-key}")
    private String apiKey;

    @Value("${ai.url}")
    private String apiUrl;

    public String ask(String question) {
        log.info("收到AI问题: {}", question);
        log.info("API URL: {}, API Key: {}", apiUrl, apiKey != null ? "已配置" : "未配置");

        Map<String, Object> body = new HashMap<>();
        body.put("model", "deepseek-ai/deepseek-v4-flash");

        List<Map<String, Object>> messages = new ArrayList<>();

        messages.add(new HashMap<String, Object>() {{
            put("role", "system");
            put("content", "你是专业营养师助手，请严格按照要求回答：" +
                    "1. 内容必须科学可靠；" +
                    "2. 不要胡编；" +
                    "3. 控制在200字以内；" +
                    "4. 用简单语言回答");
        }});

        messages.add(new HashMap<String, Object>() {{
            put("role", "user");
            put("content", question);
        }});

        body.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            log.info("开始请求AI服务...");

            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);
            log.info("AI响应状态: {}", response.getStatusCode());

            Map data = response.getBody();
            log.info("AI响应数据: {}", data);

            if (data == null) {
                return "AI服务返回空数据";
            }

            List choices = (List) data.get("choices");
            if (choices == null || choices.isEmpty()) {
                return "AI服务返回空结果";
            }

            Map first = (Map) choices.get(0);
            Map message = (Map) first.get("message");

            return (String) message.get("content");

        } catch (Exception e) {
            log.error("AI服务调用失败: {}", e.getMessage(), e);
            return "AI服务暂时不可用: " + e.getMessage();
        }
    }
}
