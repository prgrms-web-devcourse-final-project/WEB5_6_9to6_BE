package com.grepp.spring.app.model.chat.service;

import com.grepp.spring.app.controller.websocket.payload.ChatMessageRequest;
import com.grepp.spring.app.controller.websocket.payload.ChatMessageResponse;
import com.grepp.spring.app.model.chat.entity.Chat;
import com.grepp.spring.app.model.chat.entity.ChatRoom;
import com.grepp.spring.app.model.chat.repository.ChatRepository;
import com.grepp.spring.app.model.chat.repository.ChatRoomRepository;
import com.grepp.spring.infra.util.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {


    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    @Transactional
    public ChatMessageResponse saveChatMessage(Long studyId, ChatMessageRequest request,Long senderId) {
        ChatRoom chatRoom = chatRoomRepository.findByStudyId(studyId)
            .orElseThrow(() -> new NotFoundException("Invalid studyId"));

        Chat chat = request.toEntity(chatRoom,senderId);

        chatRepository.save(chat);

        return ChatMessageResponse.from(chat);


    }
}