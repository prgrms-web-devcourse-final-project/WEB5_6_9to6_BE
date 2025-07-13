package com.grepp.spring.app.controller.api.chat;


import com.grepp.spring.app.model.chat.entity.Chat;
import java.time.LocalDateTime;


public record ChatHistoryResponse (
    Long chatId,
    Long senderId,
    String senderNickname,
    String receiverId,
    String receiverNickname,
    String content,
    LocalDateTime createdAt
){
    public static ChatHistoryResponse from(Chat chat) {
        return new ChatHistoryResponse(
            chat.getId(),
            chat.getSenderId(),
            chat.getSenderNickname(),
            chat.getReceiverId(),
            chat.getReceiverNickname(),
            chat.getContent(),
            chat.getCreatedAt()
        );
    }

}
