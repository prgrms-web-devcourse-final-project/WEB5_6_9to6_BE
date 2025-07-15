package com.grepp.spring.app.model.alarm.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AlarmListResponse {

    private Long alarmId;
    private Long alarmRecipientId;
    private String type;
    private String resultStatus;
    private String message;
    private Boolean isRead;
    private LocalDateTime sentAt;

    @Builder
    public AlarmListResponse(Long alarmId, Long alarmRecipientId, String type, String resultStatus,
        String message, Boolean isRead, LocalDateTime sentAt) {
        this.alarmId = alarmId;
        this.alarmRecipientId = alarmRecipientId;
        this.type = type;
        this.resultStatus = resultStatus;
        this.message = message;
        this.isRead = isRead;
        this.sentAt = sentAt;
    }
}
