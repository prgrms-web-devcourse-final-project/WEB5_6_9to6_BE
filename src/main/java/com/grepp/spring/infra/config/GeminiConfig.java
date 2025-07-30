package com.grepp.spring.infra.config;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.api.HarmCategory;
import com.google.cloud.vertexai.api.SafetySetting;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class GeminiConfig {

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.location}")
    private String location;

    @Value("${gcp.model-name}")
    private String modelName;

    @Bean
    public VertexAI vertexAI() throws IOException {
        return new VertexAI(projectId, location);
    }

    @Bean
    public GenerationConfig generationConfig() {
        return GenerationConfig.newBuilder()
                .setTemperature(0.1f)
                .setTopP(1.0f)
                .setTopK(32)
                .setMaxOutputTokens(4096)
                .build();
    }

    @Bean
    public GenerativeModel generativeModel(VertexAI vertexAI, GenerationConfig generationConfig) {
        List<SafetySetting> safetySettings = Arrays.asList(
                SafetySetting.newBuilder()
                        .setCategory(HarmCategory.HARM_CATEGORY_HATE_SPEECH)
                        .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_ONLY_HIGH)
                        .build(),
                SafetySetting.newBuilder()
                        .setCategory(HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT)
                        .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_ONLY_HIGH)
                        .build(),
                SafetySetting.newBuilder()
                        .setCategory(HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT)
                        .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_ONLY_HIGH)
                        .build(),
                SafetySetting.newBuilder()
                        .setCategory(HarmCategory.HARM_CATEGORY_HARASSMENT)
                        .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_ONLY_HIGH)
                        .build()
        );

        return new GenerativeModel(modelName, vertexAI)
                .withGenerationConfig(generationConfig)
                .withSafetySettings(safetySettings);
    }
}