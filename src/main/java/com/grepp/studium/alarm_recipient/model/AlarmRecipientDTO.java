package com.grepp.studium.alarm_recipient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AlarmRecipientDTO {

    private Integer alarmRecipientId;

    @NotNull
    @JsonProperty("isRead")
    private Boolean isRead;

    @NotNull
    private Boolean activated;

    @NotNull
    private Integer alarm;

    @NotNull
    private Integer member;

}
