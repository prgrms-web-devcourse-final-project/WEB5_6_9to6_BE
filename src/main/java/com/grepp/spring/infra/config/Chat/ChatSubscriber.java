package com.grepp.spring.infra.config.Chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.spring.app.controller.websocket.payload.ChatMessageRequest;
import com.grepp.spring.app.controller.websocket.payload.ChatMessageResponse;
import com.grepp.spring.app.model.auth.domain.Principal;
import com.grepp.spring.app.model.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatSubscriber implements MessageListener {

    private final ChatService chatService; // DB 저장용
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String body = new String(message.getBody());

            ChatMessageRequest chat = objectMapper.readValue(body, ChatMessageRequest.class);


            // DB 저장
            // WebSocket 응답용 객체 생성
           ChatMessageResponse response=  chatService.saveChatMessage(chat.getStudyId(), chat, chat.getSenderId());



            if (chat.getReceiverEmail() == null) {
                // 전체 채팅
                messagingTemplate.convertAndSend("/subscribe/" + chat.getStudyId(), response);
            } else {
                // 귓속말
                messagingTemplate.convertAndSendToUser(chat.getReceiverEmail(), "/queue/messages", response);





                // 보낸 사람에게도 보내기(자기 메세지 확인용)
                if(!chat.getReceiverEmail().equals(chat.getSenderEmail())){
                    messagingTemplate.convertAndSendToUser(
                        chat.getSenderEmail(),
                        "/queue/messages",
                        response
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }

