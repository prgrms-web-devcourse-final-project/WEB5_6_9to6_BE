package com.grepp.spring.app.model.member.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberMyPageResponse {

    private String nickname;
    private int joinedStudyCount;
    private int rewardPoints;
    private int winCount;
    private List<MypageStudyInfoResponse> userStudies;

    @Builder
    public MemberMyPageResponse(String nickname, int joinedStudyCount, int rewardPoints,
        int winCount,
        List<MypageStudyInfoResponse> userStudies) {
        this.nickname = nickname;
        this.joinedStudyCount = joinedStudyCount;
        this.rewardPoints = rewardPoints;
        this.winCount = winCount;
        this.userStudies = userStudies;
    }
}
