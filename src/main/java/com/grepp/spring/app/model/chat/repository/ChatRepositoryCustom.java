package com.grepp.spring.app.model.chat.repository;

import com.grepp.spring.app.model.chat.entity.Chat;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRepositoryCustom {
    List<Chat> findAllRelevantChats(Long studyId, String username, Long memberId);
    List<Chat> findAllRelevantChatsPage(Long studyId, String username, Long memberId, LocalDateTime cursorCreatedAt,Long lastId, int pageSize);

}
