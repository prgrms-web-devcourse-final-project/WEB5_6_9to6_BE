package com.grepp.spring.app.controller.websocket.payload;

import com.grepp.spring.app.model.chat.entity.Chat;

public record ChatMessageResponse(
    Long senderId,
    String senderNickname,
    String receiverId,
    String receiverNickname,
    String image,
    String content) {

    public ChatMessageResponse {
        System.out.println("귓속말 대상: " + receiverId + ", 내용: " + content);
    }

    public static ChatMessageResponse from(Chat chat, String image) {
        return new ChatMessageResponse(
            chat.getSenderId(),
            chat.getSenderNickname(),
            chat.getReceiverId(),
            chat.getReceiverNickname(),
            image,
            chat.getContent()
        );
    }
}