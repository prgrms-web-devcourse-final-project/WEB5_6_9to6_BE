package com.grepp.spring.app.model.study.service;

import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.member.repository.StudyMemberRepository;
import com.grepp.spring.app.model.study.entity.GoalAchievement;
import com.grepp.spring.app.model.study.entity.StudyGoal;
import com.grepp.spring.app.model.study.reponse.GoalsResponse;
import com.grepp.spring.app.model.study.repository.GoalAchievementRepository;
import com.grepp.spring.app.model.study.repository.StudyGoalRepository;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyGoalRepository studyGoalRepository;
    private final GoalAchievementRepository goalAchievementRepository;
    private final StudyMemberRepository studyMemberRepository;

    @Transactional
    public void registGoal(List<Long> ids) {
        Long studyId = ids.get(0);
        Long memberId = ids.get(1);
        Long goalId = ids.get(2);

        StudyGoal studyGoal = studyGoalRepository.findById(goalId)
            .orElseThrow(() -> new NotFoundException("해당 목표를 찾을 수 없습니다."));
        StudyMember studyMember = studyMemberRepository.findByStudyStudyIdAndMemberId(studyId, memberId)
            .orElseThrow(() -> new NotFoundException("스터디 멤버 정보를 찾을 수 없습니다."));

        GoalAchievement newAchievement = GoalAchievement.builder()
            .studyGoal(studyGoal)
            .studyMember(studyMember)
            .isAccomplished(true)
            .activated(true)
            .achievedAt(LocalDateTime.now())
            .build();

        goalAchievementRepository.save(newAchievement);
    }

    @Transactional(readOnly = true)
    public List<GoalsResponse> findGoals(Long studyId) {
        return studyGoalRepository.findGoalsById(studyId);
    }
}
