package com.grepp.spring.app.controller.api.study.payload;

import com.grepp.spring.app.model.study.code.ApplicantState;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplicationResultRequest {

    private long memberId;
    private ApplicantState applicationResult;

    @Builder
    public ApplicationResultRequest(long memberId, ApplicantState applicationResult) {
        this.memberId = memberId;
        this.applicationResult = applicationResult;

    }

}
