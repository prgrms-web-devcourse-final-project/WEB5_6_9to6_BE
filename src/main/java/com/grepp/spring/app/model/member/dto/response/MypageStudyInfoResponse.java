package com.grepp.spring.app.model.member.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MypageStudyInfoResponse {

    private String title;
    private int currentMemberCount;
    private int maxMemberCount;
    private String category;
    private String region;
    private String place;

    private List<String> schedules;

    private String startTime;
    private String endTime;

    private String studyType;

    private List<AchievementRecordResponse> achievementRecords;

    @Builder
    public MypageStudyInfoResponse(String title, int currentMemberCount, int maxMemberCount,
        String category, String region, String place, String startDate, String endDate,
        List<String> schedules, String startTime, String endTime, String studyType,
        List<AchievementRecordResponse> achievementRecords) {
        this.title = title;
        this.currentMemberCount = currentMemberCount;
        this.maxMemberCount = maxMemberCount;
        this.category = category;
        this.region = region;
        this.place = place;
        this.schedules = schedules;
        this.startTime = startTime;
        this.endTime = endTime;
        this.studyType = studyType;
        this.achievementRecords = achievementRecords;
    }
}
