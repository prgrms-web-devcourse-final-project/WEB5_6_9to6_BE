package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.study.dto.response.CheckGoalResponse;
import com.grepp.spring.app.model.study.dto.response.WeeklyAchievementCount;
import com.grepp.spring.app.model.study.entity.StudyGoal;
import com.grepp.spring.app.model.study.reponse.GoalsResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface GoalRepositoryCustom {
    List<CheckGoalResponse> findAchieveStatuses(Long studyId, Long studyMemberId, LocalDateTime now);

    List<GoalsResponse> findStudyGoals(Long studyId);

    List<StudyGoal> findGoalsByStudyId(Long studyId);

//    int getTotalAchievementsCount(Long studyId, Long studyMemberId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<WeeklyAchievementCount> countWeeklyAchievements(Long studyId, Long studyMemberId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    boolean findSameLog(Long goalId, Long memberId, LocalDate today);
}
