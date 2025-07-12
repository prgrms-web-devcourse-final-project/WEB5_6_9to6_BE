package com.grepp.spring.app.model.chat.repository;

import com.grepp.spring.app.model.chat.entity.Chat;
import java.util.List;

public interface ChatRepositoryCustom {
    List<Chat> findAllRelevantChats(Long studyId, String username, Long memberId);

}
