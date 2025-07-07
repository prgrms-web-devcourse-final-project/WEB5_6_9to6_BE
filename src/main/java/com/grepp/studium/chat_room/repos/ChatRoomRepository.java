package com.grepp.studium.chat_room.repos;

import com.grepp.studium.chat_room.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
}
