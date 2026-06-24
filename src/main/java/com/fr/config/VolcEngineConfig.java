package com.fr.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "volcengine")
public class VolcEngineConfig {
    
    private String apiKey = "YOUR_VOLC_ENGINE_API_KEY";
    private String apiSecret = "YOUR_VOLC_ENGINE_API_SECRET";
    private String endpoint = "https://api.bytedance.net/api/text/generation";
    private String model = "Doubao";

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}