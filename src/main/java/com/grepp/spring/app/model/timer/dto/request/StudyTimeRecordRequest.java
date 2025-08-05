package com.grepp.spring.app.model.timer.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudyTimeRecordRequest {

    @NotNull(message = "공부 시간은 필수입니다.")
    @PositiveOrZero(message = "공부 시간은 0 이상이어야 합니다.")
    int studyTime;

    public StudyTimeRecordRequest(int studyTime) {
        this.studyTime = studyTime;
    }
}
