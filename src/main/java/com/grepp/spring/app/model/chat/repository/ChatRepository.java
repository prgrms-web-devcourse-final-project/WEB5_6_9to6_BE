package com.grepp.spring.app.model.chat.repository;

import com.grepp.spring.app.model.chat.entity.Chat;
import com.grepp.spring.app.model.chat.entity.ChatRoom;
import java.time.LocalDateTime;
import java.util.List;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>,ChatRepositoryCustom {

    int deleteByCreatedAtBefore(LocalDateTime threshold);
}
