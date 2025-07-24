package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.controller.api.study.payload.CheckGoalResponse;
import com.grepp.spring.app.model.study.reponse.GoalsResponse;
import java.util.List;

public interface GoalRepositoryCustom {
    List<CheckGoalResponse> findAchieveStatuses(Long studyId, Long studyMemberId);

    List<GoalsResponse> findStudyGoals(Long studyId);

//    int getTotalAchievementsCount(Long studyId, Long studyMemberId, LocalDateTime startDateTime, LocalDateTime endDateTime);

}
