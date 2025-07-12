package com.grepp.spring.app.model.study.service;

import com.grepp.spring.app.controller.api.study.payload.StudySearchRequest;
import com.grepp.spring.app.controller.api.study.payload.StudyUpdateRequest;
import com.grepp.spring.app.model.member.dto.response.ApplicantsResponse;
import com.grepp.spring.app.model.member.dto.response.StudyMemberResponse;
import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.member.repository.StudyMemberRepository;
import com.grepp.spring.app.model.study.code.DayOfWeek;
import com.grepp.spring.app.model.study.dto.StudyInfoResponse;
import com.grepp.spring.app.model.study.dto.StudyListResponse;
import com.grepp.spring.app.model.study.entity.GoalAchievement;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.app.model.study.entity.StudyGoal;
import com.grepp.spring.app.model.study.reponse.GoalsResponse;
import com.grepp.spring.app.model.study.repository.GoalAchievementRepository;
import com.grepp.spring.app.model.study.repository.StudyGoalRepository;
import com.grepp.spring.app.model.study.repository.StudyRepository;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional(readOnly = true)
    public StudyInfoResponse getStudyInfo(Long studyId) {
        Study studyWithGoals = studyRepository.findByIdWithGoals(studyId)
            .orElseThrow(() -> new NotFoundException("스터디가 존재하지 않습니다."));

        Study studyWithSchedules = studyRepository.findByIdWithSchedules(studyId)
            .orElseThrow(() -> new NotFoundException("스터디가 존재하지 않습니다."));

        // goals fetch join 과 schedules fetch join 을 합치기
        studyWithGoals.getSchedules().addAll(studyWithSchedules.getSchedules());

        return StudyInfoResponse.fromEntity(studyWithGoals);
    }

    @Transactional
    public void updateStudy(Long studyId, StudyUpdateRequest req) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));

        // 기본 정보 업데이트
        study.updateBaseInfo(
            req.getName(),
            req.getCategory(),
            req.getMaxMembers(),
            req.getRegion(),
            req.getPlace(),
            req.isOnline(),
            req.getDescription(),
            req.getExternalLink(),
            req.getStatus()
        );
        study.setEndDate(req.getEndDate());

        // 기존 일정 제거 후 다시 생성
        study.getSchedules().clear();
        for (DayOfWeek day : req.getSchedules()) {
            study.addSchedule(day, req.getStartTime(), req.getEndTime());
        }

        // 목표 업데이트 (목표 ID 기준으로 수정)
        for (StudyUpdateRequest.GoalUpdateDTO g : req.getGoals()) {
            StudyGoal goal = study.getGoals().stream()
                .filter(existing -> existing.getGoalId().equals(g.getGoalId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 목표입니다."));

            goal.update(g.getContent(), g.getType());
        }
    }

    public boolean isUserStudyMember(Long memberId, Long studyId) {
        return studyMemberRepository.existsByMember_IdAndStudy_StudyId(memberId, studyId);
    }

    // 스터디 멤버 조회
    public List<StudyMemberResponse> getStudyMembers(Long studyId) {
        if (!studyRepository.existsById(studyId)) {
            throw new NotFoundException("스터디가 존재하지 않습니다.");
        }

        List<StudyMember> studyMembers = studyMemberRepository.findAllByStudyIdWithMember(studyId);

        return studyMembers.stream()
            .map(studyMember -> {
                Member member = studyMember.getMember();
                return StudyMemberResponse.builder()
                    .studyMemberId(studyMember.getStudyMemberId())
                    .memberId(member.getId())
                    .nickName(member.getNickname())
                    .profileImage(member.getAvatarImage())
                    .role(studyMember.getStudyRole())
                    .email(member.getEmail())
                    .build();
            })
            .toList();
    }
}
