package com.grepp.spring.app.model.study.dto.request;

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
