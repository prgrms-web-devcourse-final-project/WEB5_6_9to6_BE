package com.grepp.spring.infra.config.Chat.WebSocket;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.spring.app.controller.websocket.payload.ChatMessageRequest;
import com.grepp.spring.app.controller.websocket.payload.ChatMessageResponse;
import com.grepp.spring.app.model.chat.service.ChatService;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkerManager {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    private final Map<Long, ExecutorService> workerMap = new ConcurrentHashMap<>();
    private static final int MAX_WORKERS = 10;

    public synchronized void startWorker(Long studyId) {
        // 이미 실행 중이면 무시
        if (workerMap.containsKey(studyId)) return;

        // 워커 개수 제한
        if (workerMap.size() >= MAX_WORKERS) {
            System.out.println(" 최대 워커 수 초과: studyId = " + studyId);
            return; // 또는 큐에 대기 등록 등
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        workerMap.put(studyId, executor);

        executor.submit(() -> processMessages(studyId));
    }


    // 메시지 처리 로직
    private void processMessages(Long studyId) {
        System.out.println("워커 실행 시작 for studyId = " + studyId);
        String queueKey = "chat:log:" + studyId;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String msg = redisTemplate.opsForList().rightPop(queueKey, Duration.ofSeconds(5));
                if (msg != null) {

                    System.out.printf("[studyId=%d] 메시지 처리 중: %s%n", studyId, msg);

                    try {
                        // JSON → ChatMessageRequest 객체로 변환
                        ChatMessageRequest chat = objectMapper.readValue(msg, ChatMessageRequest.class);

                        // DB 저장
                        ChatMessageResponse response = chatService.saveChatMessage(
                            chat.getStudyId(), chat, chat.getSenderId()
                        );

                        // WebSocket 브로드캐스트
                        if (chat.getReceiverEmail() == null) {
                            System.out.println("브로드캐스트: /subscribe/" + studyId + " -> " + response.content());
                            messagingTemplate.convertAndSend("/subscribe/" + chat.getStudyId(), response);
                        } else {
                            messagingTemplate.convertAndSendToUser(chat.getReceiverEmail(), "/queue/messages", response);

                            if (!chat.getReceiverEmail().equals(chat.getSenderEmail())) {
                                messagingTemplate.convertAndSendToUser(chat.getSenderEmail(), "/queue/messages", response);
                            }
                        }
                    } catch (Exception e) {
                        System.err.printf("[studyId=%d] 메시지 처리 중 오류: %s%n", studyId, e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.out.printf("[studyId=%d] 워커 에러: %s%n", studyId, e.getMessage());
        }
    }


    public synchronized void stopWorker(Long studyId) {
        ExecutorService executor = workerMap.remove(studyId);
        if (executor != null) {
            executor.shutdownNow();
            System.out.println("워커 중지: studyId = " + studyId);
        }
    }
}