package com.grepp.spring.infra.config.Chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.spring.app.controller.websocket.payload.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatPublisher {

    private final RedisTemplate<String,String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void publish(String studyId, ChatMessageRequest request){
        try {
            String message = objectMapper.writeValueAsString(request);
            System.out.println(" Redis 발행 직전: " + message);
            redisTemplate.convertAndSend("chat:"+studyId, message);
            System.out.println(" Redis 발행 완료");
        }catch (JsonProcessingException e){
            System.out.println(" Redis 발행 실패");
            throw new RuntimeException("Failed to serialize message", e);

        }


    }

}
