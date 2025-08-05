package com.grepp.spring.app.model.alarm.dto.request;

import com.grepp.spring.app.model.alarm.code.AlarmType;
import com.grepp.spring.app.model.study.code.ApplicantState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AlarmRequest {

    private Long studyId;

    @NotNull(message = "알림 발신자 ID는 필수입니다.")
    private Long senderId;

    @NotNull(message = "알림 수신자 ID는 필수입니다.")
    private Long receiverId;

    @NotBlank(message = "알림 메시지는 공백일 수 없습니다.")
    private String message;

    @NotNull(message = "알림 타입(type)은 필수입니다.")
    private AlarmType type;

    private ApplicantState resultStatus;

    @Builder
    public AlarmRequest(Long studyId, Long senderId, Long receiverId, String message,
        AlarmType type,
        ApplicantState resultStatus) {
        this.studyId = studyId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.type = type;
        this.resultStatus = resultStatus;
    }
}
