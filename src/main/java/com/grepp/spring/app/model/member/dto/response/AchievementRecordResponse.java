package com.grepp.spring.app.model.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AchievementRecordResponse {

    private boolean isAccomplished;
    private String achievedAt;

    @Builder
    public AchievementRecordResponse(boolean isAccomplished, String achievedAt) {
        this.isAccomplished = isAccomplished;
        this.achievedAt = achievedAt;
    }
}
