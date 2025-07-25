package com.grepp.spring.app.controller.api.chat;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public record ChatCursorRequest(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                LocalDateTime cursorCreatedAt,
                                Long lastChatId) {

}
