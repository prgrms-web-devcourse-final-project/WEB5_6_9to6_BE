package com.grepp.spring.app.controller.api.alarm.payload;

import com.grepp.spring.app.model.alarm.code.AlarmType;
import com.grepp.spring.app.model.alarm.code.ResultStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlarmRequest {

    private Long senderId;
    private Long receiverId;
    private String message;
    private AlarmType type;
    private ResultStatus resultStatus;
}
