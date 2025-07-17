package com.grepp.spring.app.model.study.reponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyNoticeResponse {

    private String notice;

    public StudyNoticeResponse(String notice) {
        this.notice = notice;
    }

}
