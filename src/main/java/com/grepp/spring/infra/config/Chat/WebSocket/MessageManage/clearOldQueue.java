package com.grepp.spring.infra.config.Chat.WebSocket.MessageManage;

import java.time.Duration;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;

@Scheduled(cron = "0 0 3 * * ?")
public class clearOldQueue {
    long threshold = System.currentTimeMillis() - Duration.ofDays(2).toMillis();

    for (Long studyId : tracker.getAllStudyIds()) {
        String key = "chat:log:" + studyId;
        List<String> messages = redisTemplate.opsForList().range(key, 0, -1);

        for (String msg : messages) {
            try {
                ChatWrapper wrapper = objectMapper.readValue(msg, ChatWrapper.class);
                if (wrapper.timestamp() < threshold) {
                    redisTemplate.opsForList().remove(key, 1, msg);
                }
            } catch (Exception ignored) {}
        }
    }
}
