package com.grepp.spring.app.model.alarm.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AlarmListResponse {

    private Long alarmId;
    private String type;
    private String message;
    private Boolean isRead;
    private LocalDateTime sentAt;

    @Builder
    public AlarmListResponse(Long alarmId, String type, String message, Boolean isRead,
        LocalDateTime sentAt) {
        this.alarmId = alarmId;
        this.type = type;
        this.message = message;
        this.isRead = isRead;
        this.sentAt = sentAt;
    }
}
