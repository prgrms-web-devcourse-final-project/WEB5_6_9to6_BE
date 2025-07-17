package com.grepp.spring.app.controller.api.study.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationUpdateRequest {

    String notice;

    public NotificationUpdateRequest(String notice) {
        this.notice = notice;
    }
}
