package com.grepp.studium.chat_room.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChatRoomDTO {

    private Integer roomId;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private Boolean activated;

}
