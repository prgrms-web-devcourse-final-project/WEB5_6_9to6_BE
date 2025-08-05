package com.grepp.spring.app.model.alarm.dto.response;

import com.grepp.spring.app.model.alarm.entity.Alarm;
import com.grepp.spring.app.model.alarm.entity.AlarmRecipient;
import com.grepp.spring.app.model.member.entity.Member;
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

    private Long senderId;
    private String senderNickname;
    private String senderAvatarImage;

    private Long studyId;

    @Builder
    public AlarmListResponse(AlarmRecipient recipient) {
        Alarm alarm = recipient.getAlarm();
        Member sender = alarm.getSender();

        this.alarmId = alarm.getId();
        this.alarmRecipientId = recipient.getId();
        this.type = alarm.getAlarmType().name();
        this.resultStatus = alarm.getResultStatus() != null ? alarm.getResultStatus().name() : null;
        this.message = alarm.getMessage();
        this.isRead = recipient.getIsRead();
        this.sentAt = alarm.getCreatedAt();

        this.senderId = sender.getId();
        this.senderNickname = sender.getNickname();
        this.senderAvatarImage = sender.getAvatarImage();

        this.studyId = alarm.getStudy() != null ? alarm.getStudy().getStudyId() : null;
    }
}