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
    private String avatarImage;

    public ApplicantsResponse(Long applicantId, Long memberId, String name, ApplicantState state,
        String introduction, String avatarImage) {
        this.applicantId = applicantId;
        this.memberId = memberId;
        this.name = name;
        this.state = state;
        this.introduction = introduction;
        this.avatarImage = avatarImage;
    }

}
