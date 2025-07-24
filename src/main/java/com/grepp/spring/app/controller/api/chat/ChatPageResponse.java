package com.grepp.spring.app.controller.api.chat;

import java.time.LocalDateTime;
import java.util.List;

public record ChatPageResponse(
    List<ChatHistoryResponse> messages,
    LocalDateTime lastCursorCreatedAt,
    Long lastChatId,
    boolean hasNext


) {

}
