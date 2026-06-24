package com.fr.service;

import com.fr.config.VolcEngineConfig;
import com.fr.mapper.GoodsMapper;
import com.fr.mapper.TypeMapper;
import com.fr.javaBean.Goods;
import com.fr.javaBean.Type;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AIChatService {

    private final VolcEngineConfig volcEngineConfig;
    private final RestTemplate restTemplate;
    private final GoodsMapper goodsMapper;
    private final TypeMapper typeMapper;
    
    private static final String SYSTEM_PROMPT = "你是蛋糕店系统的AI客服助手。你的职责是帮助用户解答关于蛋糕店的问题，包括：\n" +
            "1. 回答用户关于我们蛋糕店的问题\n" +
            "2. 推荐蛋糕商品（只能推荐本店已有的蛋糕）\n" +
            "3. 回答订单、物流、支付等问题\n" +
            "4. 提供售后服务咨询\n" +
            "\n" +
            "重要规则：\n" +
            "- 只推荐本店现有的蛋糕商品，不要虚构不存在的蛋糕\n" +
            "- 如果用户问的蛋糕本店没有，要如实告知\n" +
            "- 用友好、热情的语气与用户交流\n" +
            "- 回答要简洁、专业\n" +
            "- 如果遇到无法回答的问题，引导用户联系人工客服\n" +
            "\n" +
            "请记住：你是一家蛋糕店的AI客服，不是通用AI助手。";

    public AIChatService(VolcEngineConfig volcEngineConfig, GoodsMapper goodsMapper, TypeMapper typeMapper) {
        this.volcEngineConfig = volcEngineConfig;
        this.goodsMapper = goodsMapper;
        this.typeMapper = typeMapper;
        this.restTemplate = new RestTemplate();
    }

    public String chat(String userMessage) {
        try {
            String apiKey = volcEngineConfig.getApiKey();
            String apiSecret = volcEngineConfig.getApiSecret();
            
            if ("YOUR_VOLC_ENGINE_API_KEY".equals(apiKey)) {
                return "请先在配置文件中设置火山引擎API Key";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);
            if (!"YOUR_VOLC_ENGINE_API_SECRET".equals(apiSecret) && apiSecret != null && !apiSecret.isEmpty()) {
                headers.set("X-Volc-Secret", apiSecret);
            }
            
            List<Map<String, Object>> messages = new ArrayList<>();
            
            String systemMessage = SYSTEM_PROMPT + "\n\n【本店商品信息】\n" + getProductInfo();
            Map<String, Object> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", systemMessage);
            messages.add(systemMsg);
            
            Map<String, Object> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);
            messages.add(userMsg);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", volcEngineConfig.getModel());
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 2048);
            requestBody.put("temperature", 0.7);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                    volcEngineConfig.getEndpoint(),
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("choices")) {
                    List<Map<String, Object>> choices = 
                            (List<Map<String, Object>>) responseBody.get("choices");
                    if (!choices.isEmpty()) {
                        Map<String, Object> choice = choices.get(0);
                        if (choice.containsKey("message")) {
                            Map<String, Object> msg = (Map<String, Object>) choice.get("message");
                            if (msg.containsKey("content")) {
                                return msg.get("content").toString();
                            }
                        } else if (choice.containsKey("text")) {
                            return choice.get("text").toString();
                        }
                    }
                }
                return responseBody != null ? responseBody.toString() : "未知响应格式";
            }
            
            return "API请求失败: " + response.getStatusCode();
            
        } catch (Exception e) {
            return "AI服务暂时不可用，请稍后重试。错误信息: " + e.getMessage();
        }
    }
    
    private String getProductInfo() {
        StringBuilder info = new StringBuilder();
        try {
            List<Goods> allGoods = goodsMapper.selectList(null);
            if (allGoods != null && !allGoods.isEmpty()) {
                info.append("本店商品列表：\n");
                for (Goods goods : allGoods) {
                    info.append("- ").append(goods.getName())
                        .append("，价格：¥").append(goods.getPrice())
                        .append("，库存：").append(goods.getStock()).append("件\n");
                    if (goods.getIntro() != null && !goods.getIntro().isEmpty()) {
                        info.append("  简介：").append(goods.getIntro()).append("\n");
                    }
                }
            } else {
                info.append("暂无商品信息\n");
            }
            
            List<Type> allTypes = typeMapper.selectList(null);
            if (allTypes != null && !allTypes.isEmpty()) {
                info.append("\n蛋糕分类：");
                String types = allTypes.stream()
                    .map(Type::getName)
                    .collect(Collectors.joining("、"));
                info.append(types);
            }
        } catch (Exception e) {
            info.append("无法获取商品信息\n");
        }
        return info.toString();
    }
}