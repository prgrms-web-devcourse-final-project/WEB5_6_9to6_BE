package com.grepp.spring.app.controller.websocket.payload;

import com.grepp.spring.app.model.chat.entity.Chat;
import com.grepp.spring.app.model.chat.entity.ChatRoom;

public record   ChatMessageRequest( Long receiverId, String content) {

    public Chat toEntity(ChatRoom chatRoom, Long senderId) {
        return Chat.builder()
            .chatRoom(chatRoom)
            .senderId(senderId)
            .receiverId(receiverId)
            .content(content)
            .build();
    }
}
