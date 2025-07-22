package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.controller.api.study.payload.CheckGoalResponse;
import java.util.List;

public interface GoalQueryRepository {
    List<CheckGoalResponse> findAchieveStatusesByStudyId(Long studyId,Long studyMemberId);

}
