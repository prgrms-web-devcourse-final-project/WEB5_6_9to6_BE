package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.controller.api.study.payload.CheckGoalResponse;
import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.study.entity.GoalAchievement;
import com.grepp.spring.app.model.study.reponse.GoalsResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface GoalRepositoryCustom {
    List<CheckGoalResponse> findAchieveStatusesByStudyId(Long studyId,Long studyMemberId);

    List<GoalsResponse> findGoalsById(Long studyId);

    int countTotalAchievements(Long studyId, Long studyMemberId, LocalDateTime startDateTime, LocalDateTime endDateTime);

}
