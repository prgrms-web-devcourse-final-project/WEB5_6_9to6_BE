package com.grepp.spring.app.model.chat.entity;

import com.grepp.spring.infra.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name="chat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    private long receiverId;
    private long senderId;
    private String content;


    @Builder
    public Chat(long id, ChatRoom chatRoom, long receiverId, long senderId, String content) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.content = content;
    }
}
