package com.grepp.spring.app.controller.api.alarm.payload;

import com.grepp.spring.app.model.alarm.code.AlarmType;
import com.grepp.spring.app.model.alarm.code.ResultStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AlarmRequest {

    private Long senderId;

    @NotNull
    private Long receiverId;

    @NotBlank
    private String message;

    @NotNull
    private AlarmType type;

    private ResultStatus resultStatus;
}
