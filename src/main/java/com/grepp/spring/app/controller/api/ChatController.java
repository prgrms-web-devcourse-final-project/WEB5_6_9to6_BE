package com.grepp.spring.app.controller.api;

import com.grepp.spring.app.controller.api.chat.ChatHistoryResponse;
import com.grepp.spring.app.controller.api.chat.ParticipantResponse;
import com.grepp.spring.app.model.auth.domain.Principal;
import com.grepp.spring.app.model.chat.service.ChatService;
import com.grepp.spring.app.model.study.service.StudyService;
import com.grepp.spring.infra.config.Chat.WebSocket.WebSocketSessionTracker;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.response.ResponseCode;
import com.grepp.spring.infra.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "채팅 API", description = "스터디 채팅 내역 조회 및 참여자 목록 관련 API 입니다.")
@RestController
@RequestMapping(value = "/api/v1/chats", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final WebSocketSessionTracker webSocketSessionTracker;
    private final StudyService studyService;



    // 웹소켓 사용으로 인해 사용안함.
    // 채팅 메시지 전송
    @Operation(summary = "채팅 메시지 전송 (미사용)", description = """
        (현재 웹소켓을 사용하므로 이 API는 사용되지 않습니다.)
        요청 body에 `채팅 정보`를 포함해야합니다.
        - HTTP를 통해 채팅 메시지를 전송하는 API입니다.
        """)
    @PostMapping("/{studyId}")
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
    @Operation(summary = "채팅방 참여자 목록 조회 (미사용)", description = "(현재 웹소켓을 사용하므로 이 API는 사용되지 않을 수 있습니다.) 특정 스터디 채팅방(`studyId`)에 현재 접속 중인 사용자 목록을 조회합니다.")
    @GetMapping("/{studyId}/participants")
    public ResponseEntity<CommonResponse<List<ParticipantResponse>>> getOnlineParticipants(@PathVariable Long studyId) {


        List<ParticipantResponse> participants = chatService.getOnlineParticipants(studyId);


        return ResponseEntity.ok(CommonResponse.success(participants));
    }

    // 채팅 내역 조회
    @Operation(summary = "채팅 내역 조회", description = """
        특정 스터디(`studyId`)의 전체 채팅 내역을 조회합니다.
        - 이 API는 스터디 멤버만 호출할 수 있으며, 인증이 필요합니다.
        - 요청 헤더에 유효한 토큰이 있어야 합니다.
        """)
    @GetMapping("/{studyId}/history")
    public ResponseEntity<CommonResponse<List<ChatHistoryResponse>>> getChatHistory(@PathVariable Long studyId,
        Authentication authentication) {

        List<Map<String, Object>> chatHistory = new ArrayList<>();
        Long memberId = SecurityUtil.getCurrentMemberId();
        Principal principal = (Principal) authentication.getPrincipal();

        boolean isMember = studyService.isUserStudyMember(memberId, studyId);

        if(!isMember){
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse.error(ResponseCode.UNAUTHORIZED));
        }


        List<ChatHistoryResponse> responses = chatService.findChat(studyId,memberId,principal.getUsername());

        return ResponseEntity.ok(CommonResponse.success(responses));
    }

}
