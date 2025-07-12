package com.grepp.spring.app.controller.api.timer.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudyTimeRecordRequest {

    int studyTime;

    public StudyTimeRecordRequest(int studyTime) {
        this.studyTime = studyTime;
    }
}
