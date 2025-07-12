package com.grepp.spring.app.model.chat.repository;

import com.grepp.spring.app.model.chat.entity.ChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByStudyId(Long studyId);
}
