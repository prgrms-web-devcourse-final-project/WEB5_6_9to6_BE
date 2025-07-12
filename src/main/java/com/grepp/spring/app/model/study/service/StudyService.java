package com.grepp.spring.app.model.study.service;

import com.grepp.spring.app.controller.api.study.payload.StudySearchRequest;
import com.grepp.spring.app.model.member.dto.response.ApplicantsResponse;
import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.member.repository.StudyMemberRepository;
import com.grepp.spring.app.model.study.dto.StudyListResponse;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.app.model.study.repository.StudyRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyGoalRepository studyGoalRepository;
    private final GoalAchievementRepository goalAchievementRepository;
    private final StudyMemberRepository studyMemberRepository;

    //필터 조건에 따라 스터디 목록 + 현재 인원 수 조회
    public List<StudyListResponse> searchStudiesWithMemberCount(StudySearchRequest req) {
        List<Study> studies = studyRepository.searchByFilterWithSchedules(req);

        return studies.stream()
            .map(study -> {
                int currentMemberCount = studyMemberRepository.countByStudy_StudyId(study.getStudyId());
                return StudyListResponse.fromEntity(study, currentMemberCount);
            })
            .collect(Collectors.toList());
    }

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

    // 스터디 지원자 목록 조회
    @Transactional(readOnly = true)
    public List<ApplicantsResponse> getApplicants(Long studyId) {
        return studyRepository.findAllApplicants(studyId);
    }

    @Transactional(readOnly = true)
    public List<GoalsResponse> findGoals(Long studyId) {
        return studyGoalRepository.findGoalsById(studyId);
    }

}
