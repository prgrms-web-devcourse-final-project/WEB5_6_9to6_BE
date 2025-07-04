package com.grepp.studium.chat.repos;

import com.grepp.studium.chat.domain.Chat;
import com.grepp.studium.chat_room.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRepository extends JpaRepository<Chat, Integer> {

    Chat findFirstByRoom(ChatRoom chatRoom);

}
