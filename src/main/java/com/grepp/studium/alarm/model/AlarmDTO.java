package com.grepp.studium.alarm.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AlarmDTO {

    private Integer alarmId;

    @NotNull
    @Size(max = 255)
    private String receiver;

    @Size(max = 255)
    private String sender;

    @NotNull
    @Size(max = 255)
    private String message;

    @NotNull
    @Size(max = 255)
    private String type;

    @NotNull
    private Boolean activated;

    @NotNull
    private LocalDateTime createdAt;

}
