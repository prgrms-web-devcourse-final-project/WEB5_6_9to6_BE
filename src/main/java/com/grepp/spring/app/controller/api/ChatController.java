package com.grepp.spring.app.controller.api;

import com.grepp.spring.app.controller.api.chat.ChatHistoryResponse;
import com.grepp.spring.app.controller.api.chat.ParticipantResponse;
import com.grepp.spring.app.model.auth.domain.Principal;
import com.grepp.spring.app.model.chat.service.ChatService;
import com.grepp.spring.infra.config.Chat.WebSocket.WebSocketSessionTracker;
import com.grepp.spring.infra.response.CommonResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/chats", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final WebSocketSessionTracker webSocketSessionTracker;



    // 웹소켓 사용으로 인해 사용안함.
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


    // 웹소켓으로 변경해서 안쓸 것 같음
    // 현재 접속 중인 사용자 목록 조회
    @GetMapping("/{studyId}/participants")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<List<ParticipantResponse>>> getOnlineParticipants(@PathVariable Long studyId) {


        List<ParticipantResponse> participants = chatService.getOnlineParticipants(studyId);


        return ResponseEntity.ok(CommonResponse.success(participants));
    }

    // 채팅 내역 조회
    @GetMapping("/{studyId}/history")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<List<ChatHistoryResponse>>> getChatHistory(@PathVariable Long studyId,
        Authentication authentication) {

        List<Map<String, Object>> chatHistory = new ArrayList<>();

        Principal principal = (Principal) authentication.getPrincipal();




        List<ChatHistoryResponse> responses = chatService.findChat(studyId,principal.getMemberId(),principal.getUsername());

        return ResponseEntity.ok(CommonResponse.success(responses));
    }

}
