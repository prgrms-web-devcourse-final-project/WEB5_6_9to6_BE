package com.grepp.spring.app.model.member.dto.response;

import com.grepp.spring.app.model.study.code.ApplicantState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @ToString
@NoArgsConstructor
public class ApplicantsResponse {

    private Long applicantId;
    private Long memberId;
    private String name;
    private ApplicantState state;
    private String introduction;

    public ApplicantsResponse(Long applicantId, Long memberId, String name, ApplicantState state,
        String introduction) {
        this.applicantId = applicantId;
        this.memberId = memberId;
        this.name = name;
        this.state = state;
        this.introduction = introduction;
    }

}
