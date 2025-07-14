package com.grepp.spring.infra.config.Chat.WebSocket;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisConnectionChecker {
    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void checkConnection() {
        try {
            redisTemplate.opsForValue().set("connection_test", "ok", Duration.ofSeconds(10));
            String result = redisTemplate.opsForValue().get("connection_test");
            System.out.println("✅ Redis 연결 테스트 성공: " + result);
        } catch (Exception e) {
            System.err.println("❌ Redis 연결 실패!");
            e.printStackTrace();
        }
    }
}
