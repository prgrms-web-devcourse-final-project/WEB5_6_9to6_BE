package com.grepp.spring.app.controller.websocket.payload;

import com.grepp.spring.app.model.chat.entity.Chat;

public record ChatMessageResponse(Long senderId, Long receiverId, String content) {

    public static ChatMessageResponse from(Chat chat) {
        return new ChatMessageResponse(
            chat.getSenderId(),
            chat.getReceiverId(),
            chat.getContent()
        );
    }
}