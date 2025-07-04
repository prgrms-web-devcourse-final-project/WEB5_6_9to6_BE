package com.grepp.studium.chat.domain;

import com.grepp.studium.chat_room.domain.ChatRoom;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Chats")
@Getter
@Setter
public class Chat {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Integer chatId;

    @Column
    private Integer receiverId;

    @Column(nullable = false)
    private Integer senderId;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Column(nullable = false)
    private Boolean activated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom room;

}
