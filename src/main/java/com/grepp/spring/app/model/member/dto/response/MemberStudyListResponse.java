package com.grepp.spring.app.model.member.dto.response;

import com.grepp.spring.app.model.member.dto.StudySummaryDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberStudyListResponse {

    private Long memberId;
    private String nickname;
    private List<StudySummaryDto> studies;

}
