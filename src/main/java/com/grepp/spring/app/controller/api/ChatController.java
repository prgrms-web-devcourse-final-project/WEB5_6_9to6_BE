package com.grepp.spring.app.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/chats", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatController {

    @PostMapping("/{studyId}")
    public ResponseEntity<Map<String, Object>> sendChatMessage(@RequestBody Map<String, Object> request) {

        // 모킹용 request 필드 추출. 이건 필요한가요? 잘 모르겠습니다.
        Long senderId = ((Number) request.get("senderId")).longValue();
        Long receiverId = request.get("receiverId") != null ? ((Number) request.get("receiverId")).longValue() : null;
        String message = (String) request.get("message");
        String sendAt = (String) request.get("sendAt");

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "채팅 메시지 전송 성공");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{studyId}/participants")
    public ResponseEntity<Map<String, Object>> getOnlineParticipants(@PathVariable Long studyId) {

        List<Map<String, Object>> participants = new ArrayList<>();

        Map<String, Object> member1 = new HashMap<>();
        member1.put("memberId", 3L);
        member1.put("nickname", "철수");
        member1.put("status", "ONLINE");

        Map<String, Object> member2 = new HashMap<>();
        member2.put("memberId", 5L);
        member2.put("nickname", "영희");
        member2.put("status", "ONLINE");

        participants.add(member1);
        participants.add(member2);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "접속자 목록 조회 성공");
        response.put("data", participants);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{studyId}/history")
    public ResponseEntity<Map<String, Object>> getChatHistory(@PathVariable Long studyId) {
        List<Map<String, Object>> chatHistory = new ArrayList<>();

        Map<String, Object> chat1 = new HashMap<>();
        chat1.put("chatId", 10L);
        chat1.put("senderId", 3L);
        chat1.put("receiverId", null); // 공개 채팅
        chat1.put("nickname", "철수");
        chat1.put("message", "안녕하세요!");
        chat1.put("sentAt", "2025-07-04T14:00:00");

        Map<String, Object> chat2 = new HashMap<>();
        chat2.put("chatId", 11L);
        chat2.put("senderId", 3L);
        chat2.put("receiverId", 5L); // 귓속말
        chat2.put("nickname", "철수");
        chat2.put("message", "비밀 얘기입니다.");
        chat2.put("sentAt", "2025-07-04T14:02:00");

        chatHistory.add(chat1);
        chatHistory.add(chat2);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "채팅 내역 조회 성공");
        response.put("data", chatHistory);

        return ResponseEntity.ok(response);
    }

}
