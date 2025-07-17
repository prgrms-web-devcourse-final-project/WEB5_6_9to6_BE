package com.grepp.spring.app.controller.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.spring.app.controller.api.chat.ParticipantResponse;
import com.grepp.spring.app.controller.websocket.payload.ChatMessageRequest;
import com.grepp.spring.app.model.auth.domain.Principal;
import com.grepp.spring.app.model.chat.service.ChatService;
import com.grepp.spring.app.model.study.service.StudyService;
import com.grepp.spring.infra.config.Chat.WebSocket.WorkerManager;
import com.grepp.spring.infra.response.CommonResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebsocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String , String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final WorkerManager workerManager;



//    @MessageMapping("/chat/{studyId}")
//    public void sendChatMessageWs(
//        Authentication authentication,
//        @DestinationVariable Long studyId,
//        ChatMessageRequest request,
//        @Header("simpUser") UsernamePasswordAuthenticationToken token) {
//
//        System.out.println("WebSocket 연결된 사용자: " + authentication.getName());
//
//        Principal principal = (Principal) token.getPrincipal();
//
//        if (principal == null) {
//            // 인증 실패 처리
//            return;
//        }
//
//
//
//        long memberId = principal.getMemberId();
//
//// 채팅 저장 및 응답 생성
//        ChatMessageResponse response = chatService.saveChatMessage(studyId, request, memberId);
//
//
//        if (request.getReceiverId() == null) {
//            // 전체 채팅방 브로드캐스트
//            messagingTemplate.convertAndSend("/subscribe/" + studyId, response);
//        }
//        else {
//
//
//
//         authentication.getPrincipal();
//
//         System.out.println("request"+request.getReceiverId());
//            // 1:1 귓속말 - 수신자1
//            messagingTemplate.convertAndSendToUser(
//
//                request.getReceiverId(),
//                "/queue/messages",
//                response
//            );
//
//
//            if(!(authentication.getName().equals(request.getReceiverId())) ) {
//                // 보낸 사람에게도 보냄 (자기 메시지 확인용)
//                messagingTemplate.convertAndSendToUser(
//                    principal.getUsername() + "",
//                    "/queue/messages",
//                    response
//                );
//            }
//
//        }
//    }

    @MessageMapping("/participants/{studyId}")
    public void requestParticipants(@DestinationVariable Long studyId, Principal principal) {



        List<ParticipantResponse> participants = chatService.getOnlineParticipants(studyId);
        CommonResponse<List<ParticipantResponse>> response = CommonResponse.success(participants);
        messagingTemplate.convertAndSend("/subscribe/" + studyId + "/participants", response);
    }

// 웹소켓으로 받은 메세지를 레디스로 전송
    @MessageMapping("/chat.send/{studyId}") // ex. /publish/chat.send
    public void sendMessage(ChatMessageRequest request,
        @DestinationVariable Long studyId,
        Authentication authentication) {


        System.out.println("WebSocket message received for studyId = " + studyId);

        Principal principal = (Principal) authentication.getPrincipal();


        long memberId = principal.getMemberId();


        request.setSenderId(memberId);
        request.setStudyId(studyId);
        request.setSenderEmail(principal.getUsername());

        System.out.println(" Redis 발행 직전: " + request);
        String message = null;
        try {
            message = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String topic = "chat:" + studyId;
        Long publishedCount= redisTemplate.convertAndSend(topic , message);
        redisTemplate.opsForList().leftPush("chat:log:"+studyId, message); // List 저장

        System.out.println(" Redis 발행 완료, 수신 리스너 수: " + publishedCount);
    }

}