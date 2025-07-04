package com.grepp.studium.chat_room.domain;

import com.grepp.studium.chat.domain.Chat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "ChatRooms")
@Getter
@Setter
public class ChatRoom {

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
    private Integer roomId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean activated;

    @OneToMany(mappedBy = "room")
    private Set<Chat> roomChats = new HashSet<>();

}
