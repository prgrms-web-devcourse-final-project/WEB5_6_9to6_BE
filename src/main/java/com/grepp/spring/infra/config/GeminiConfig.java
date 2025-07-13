package com.grepp.spring.infra.config;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;

@Configuration
public class GeminiConfig {

    private final String projectId = "able-coast-463406-n9";
    private final String location = "us-central1";
    private final String modelName = "gemini-2.5-pro-preview-03-25";

    @Bean
    public VertexAI vertexAI() throws IOException {
        return new VertexAI(projectId, location);
    }

    @Bean
    public GenerationConfig generationConfig() {
        return GenerationConfig.newBuilder()
                .setMaxOutputTokens(2048)
                .setTemperature(0.4f)
                .setTopK(32)
                .setTopP(1)
                .build();
    }

    @Bean
    public GenerativeModel generativeModel(VertexAI vertexAI, GenerationConfig generationConfig) {
        return new GenerativeModel.Builder()
                .setModelName(modelName)
                .setVertexAi(vertexAI)
                .setGenerationConfig(generationConfig)
                .build();
    }
}