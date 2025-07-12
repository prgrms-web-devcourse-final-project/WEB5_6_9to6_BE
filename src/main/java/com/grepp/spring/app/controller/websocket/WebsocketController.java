package com.grepp.spring.app.controller.websocket;

import com.grepp.spring.app.controller.websocket.payload.ChatMessageRequest;
import com.grepp.spring.app.controller.websocket.payload.ChatMessageResponse;
import com.grepp.spring.app.model.auth.domain.Principal;
import com.grepp.spring.app.model.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebsocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/chat/{studyId}")

    public void sendChatMessageWs(@DestinationVariable Long studyId,
        ChatMessageRequest request,
        @Header("simpUser") Principal principal) {

        if (principal == null) {
            // 인증 실패 처리
            return;
        }

        Principal principal = (Principal) authentication.getPrincipal();
        long memberId = principal.getMemberId();

// 채팅 저장 및 응답 생성
        ChatMessageResponse response = chatService.saveChatMessage(studyId, request, memberId);

        if (request.receiverId() == null) {
            // 전체 채팅방 브로드캐스트
            messagingTemplate.convertAndSend("/subscribe/" + studyId, response);
        } else {
            // 1:1 귓속말 - 수신자
            messagingTemplate.convertAndSendToUser(
                request.receiverId().toString(),
                "/queue/messages",
                response
            );

            // 보낸 사람에게도 보냄 (자기 메시지 확인용)
            messagingTemplate.convertAndSendToUser(
                memberId + "",
                "/queue/messages",
                response
            );


        }
    }
}