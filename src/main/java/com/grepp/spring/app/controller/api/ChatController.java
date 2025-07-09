package com.grepp.spring.app.controller.api;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
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

    // 채팅 메시지 전송
    @PostMapping("/{studyId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> sendChatMessage(@PathVariable Long studyId,
        @RequestBody Map<String, Object> request) {

        Long senderId = ((Number) request.get("senderId")).longValue();
        Long receiverId = request.get("receiverId") != null ? ((Number) request.get("receiverId")).longValue() : null;
        String message = (String) request.get("message");
        String sendAt = (String) request.get("sendAt");

        if (message == null || message.trim().isEmpty()) {
            return errorResponse("bad_request", "메시지를 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        if ("throw500".equalsIgnoreCase(message)) {
            return errorResponse("server_error", "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", "0000");
        response.put("message", "채팅 메시지 전송 성공");

        return ResponseEntity.ok(response);
    }

    // 공통 에러 응답 메서드
    private ResponseEntity<Map<String, Object>> errorResponse(String code, String message, HttpStatus status) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("code", code);
        error.put("message", message);
        return ResponseEntity.status(status).body(error);
    }

    // 현재 접속 중인 사용자 목록 조회
    @GetMapping("/{studyId}/participants")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> getOnlineParticipants(@PathVariable Long studyId) {

        List<Map<String, Object>> participants = new ArrayList<>();

        Map<String, Object> member1 = new LinkedHashMap<>();
        member1.put("memberId", 3);
        member1.put("nickname", "철수");
        member1.put("status", "ONLINE");

        Map<String, Object> member2 = new LinkedHashMap<>();
        member2.put("memberId", 5);
        member2.put("nickname", "영희");
        member2.put("status", "ONLINE");

        participants.add(member1);
        participants.add(member2);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", "0000");
        response.put("message", "접속자 목록 조회 성공");
        response.put("data", participants);

        return ResponseEntity.ok(response);
    }

    // 채팅 내역 조회
    @GetMapping("/{studyId}/history")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> getChatHistory(@PathVariable Long studyId) {

        List<Map<String, Object>> chatHistory = new ArrayList<>();

        Map<String, Object> chat1 = new LinkedHashMap<>();
        chat1.put("chatId", 10);
        chat1.put("senderId", 3);
        chat1.put("receiverId", null); // 공개 채팅
        chat1.put("nickname", "철수");
        chat1.put("message", "안녕하세요!");
        chat1.put("sentAt", "2025-07-04T14:00:00");

        Map<String, Object> chat2 = new LinkedHashMap<>();
        chat2.put("chatId", 11);
        chat2.put("senderId", 3);
        chat2.put("receiverId", 5); // 귓속말
        chat2.put("nickname", "철수");
        chat2.put("message", "비밀 얘기입니다.");
        chat2.put("sentAt", "2025-07-04T14:02:00");

        chatHistory.add(chat1);
        chatHistory.add(chat2);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", "0000");
        response.put("message", "채팅 내역 조회 성공");
        response.put("data", chatHistory);

        return ResponseEntity.ok(response);
    }

}
