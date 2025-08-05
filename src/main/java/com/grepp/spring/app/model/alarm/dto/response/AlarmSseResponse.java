package com.grepp.spring.app.model.alarm.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grepp.spring.app.model.alarm.code.AlarmType;
import com.grepp.spring.app.model.applicant.code.ApplicantState;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlarmSseResponse {

    private Long alarmRecipientId;
    private AlarmType type;
    private String message;
    private LocalDateTime sentAt;
    private Boolean isRead;
    private Long studyId;
    private ApplicantState resultStatus; // AlarmType이 RESULT일 때만 존재

    // 알림 보낸 사람 정보
    private Long senderId;
    private String senderNickname;
    private String senderAvatarImage;

}
