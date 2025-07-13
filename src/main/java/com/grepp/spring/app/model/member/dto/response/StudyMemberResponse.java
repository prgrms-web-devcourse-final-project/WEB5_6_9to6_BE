package com.grepp.spring.app.model.member.dto.response;

import com.grepp.spring.app.model.member.code.StudyRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyMemberResponse {
    private Long studyMemberId;
    private Long memberId;
    private String nickName;
    private String profileImage;
    private StudyRole role;
    private String email;
}
