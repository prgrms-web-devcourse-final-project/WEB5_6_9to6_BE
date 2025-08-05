package com.grepp.spring.app.model.applicant.dto.request;

import com.grepp.spring.app.model.applicant.code.ApplicantState;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplicationResultRequest {

    private long memberId;
    private ApplicantState applicationResult = null;

    @Builder
    public ApplicationResultRequest(long memberId, ApplicantState applicationResult) {
        this.memberId = memberId;
        this.applicationResult = applicationResult;

    }

}
