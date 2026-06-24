package com.fr.controller;

import com.fr.service.AIChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AIChatController {

    private final AIChatService aiChatService;

    public AIChatController(AIChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String message = request.get("message");
        
        if (message == null || message.trim().isEmpty()) {
            response.put("success", false);
            response.put("error", "请输入消息内容");
            return response;
        }

        String reply = aiChatService.chat(message);
        
        response.put("success", true);
        response.put("reply", reply);
        return response;
    }
}