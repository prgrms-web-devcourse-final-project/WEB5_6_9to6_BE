package com.grepp.spring.app.model.chat.entity;

import com.grepp.spring.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name="chat",
    indexes = {
        @Index(name = "idx_chat_room_created_id", columnList = "room_id, created_at, id")
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @Column(nullable = true)
    private String receiverId;
    private String receiverNickname;
    private long senderId;
    private String senderNickname;
    private String content;


    @Builder
    public Chat(long id, ChatRoom chatRoom, String receiverId, String receiverNickname, long senderId, String senderNickname, String content) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.receiverId = receiverId;
        this.receiverNickname = receiverNickname;
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.content = content;
    }
}
