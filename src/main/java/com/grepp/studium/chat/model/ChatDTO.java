package com.grepp.studium.chat.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChatDTO {

    private Integer chatId;

    private Integer receiverId;

    @NotNull
    private Integer senderId;

    @NotNull
    private String content;

    @NotNull
    private LocalDateTime sentAt;

    @NotNull
    private Boolean activated;

    @NotNull
    private Integer room;

}
