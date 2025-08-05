package com.grepp.spring.app.model.studyMmeber.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckGoalResponse {

    private Long goalId;
    private String content;
    private boolean achieved;

    @Builder
    public CheckGoalResponse(Long goalId, String content, boolean achieved) {
        this.goalId = goalId;
        this.content = content;
        this.achieved = achieved;
    }

}
