package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.controller.api.study.payload.CheckGoalResponse;
import com.grepp.spring.app.controller.api.study.payload.WeeklyAchievementCount;
import com.grepp.spring.app.model.study.reponse.GoalsResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface GoalRepositoryCustom {
    List<CheckGoalResponse> findAchieveStatuses(Long studyId, Long studyMemberId, LocalDateTime now);

    List<GoalsResponse> findStudyGoals(Long studyId);

//    int getTotalAchievementsCount(Long studyId, Long studyMemberId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<WeeklyAchievementCount> countWeeklyAchievements(Long studyId, Long studyMemberId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
