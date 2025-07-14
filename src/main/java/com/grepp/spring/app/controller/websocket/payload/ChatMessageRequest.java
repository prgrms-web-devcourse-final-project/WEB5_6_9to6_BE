package com.grepp.spring.app.controller.websocket.payload;

import com.grepp.spring.app.model.chat.entity.Chat;
import com.grepp.spring.app.model.chat.entity.ChatRoom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRequest {

    private Long senderId;
    private Long studyId;
    private String receiverEmail;
    private String receiverNickName;
    private String content;
    private String senderEmail;


    public Chat toEntity(ChatRoom chatRoom, Long senderId,String senderNickname) {
        return Chat.builder()
            .chatRoom(chatRoom)
            .senderId(senderId)
            .senderNickname(senderNickname)
            .receiverId(receiverId)
            .receiverNickname(receiverNickName)
            .content(content)
            .build();
    }
}