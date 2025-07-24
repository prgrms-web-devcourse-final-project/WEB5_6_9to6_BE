package com.grepp.spring.app.model.study.service;

import com.grepp.spring.app.controller.api.study.payload.StudyCreationRequest;
import com.grepp.spring.app.controller.api.study.payload.StudySearchRequest;
import com.grepp.spring.app.controller.api.study.payload.StudyUpdateRequest;
import com.grepp.spring.app.model.member.code.StudyRole;
import com.grepp.spring.app.model.member.dto.response.ApplicantsResponse;
import com.grepp.spring.app.model.member.dto.response.StudyMemberResponse;
import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import com.grepp.spring.app.model.member.repository.StudyMemberRepository;
import com.grepp.spring.app.model.study.code.ApplicantState;
import com.grepp.spring.app.model.study.code.DayOfWeek;
import com.grepp.spring.app.model.study.code.GoalType;
import com.grepp.spring.app.model.study.code.Status;
import com.grepp.spring.app.model.study.code.StudyType;
import com.grepp.spring.app.model.study.dto.StudyCreationResponse;
import com.grepp.spring.app.model.study.dto.StudyInfoResponse;
import com.grepp.spring.app.model.study.dto.StudyListResponse;
import com.grepp.spring.app.model.study.dto.WeeklyGoalStatusResponse;
import com.grepp.spring.app.model.study.entity.Applicant;
import com.grepp.spring.app.model.study.entity.GoalAchievement;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.app.model.study.entity.StudyGoal;
import com.grepp.spring.app.model.study.reponse.GoalsResponse;
import com.grepp.spring.app.model.study.repository.ApplicantRepository;
import com.grepp.spring.app.model.study.repository.GoalAchievementRepository;
import com.grepp.spring.app.model.study.repository.StudyGoalRepository;
import com.grepp.spring.app.model.study.repository.StudyRepository;
import com.grepp.spring.infra.error.exceptions.HasNotRightException;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import com.grepp.spring.infra.error.exceptions.StudyDataException;
import com.grepp.spring.infra.response.ResponseCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyGoalRepository studyGoalRepository;
    private final GoalAchievementRepository goalAchievementRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final MemberRepository memberRepository;
    private final ApplicantRepository applicantRepository;

    //필터 조건에 따라 스터디 목록 + 현재 인원 수 조회
    @Transactional(readOnly = true)
    public List<StudyListResponse> searchStudiesWithMemberCount(StudySearchRequest req) {
        if (req == null) {
            throw new StudyDataException(ResponseCode.BAD_REQUEST);
        }

        Page<Study> studies;
        try {
            studies = studyRepository.searchStudiesPage(req, req.getPageable());
        } catch (Exception e) {
            throw new StudyDataException(ResponseCode.FAIL_SEARCH_STUDY);
        }

        return studies.getContent().stream()
            .map(study -> {
                if (study.getStudyId() == null) {
                    throw new StudyDataException(ResponseCode.FAIL_SEARCH_STUDY);
                }

                int currentMemberCount;
                try {
                    currentMemberCount = studyMemberRepository.countByStudy_StudyId(study.getStudyId());
                } catch (Exception e) {
                    throw new StudyDataException(ResponseCode.FAIL_SEARCH_STUDY);
                }

                try {
                    return StudyListResponse.fromEntity(study, currentMemberCount);
                } catch (Exception e) {
                    throw new StudyDataException(ResponseCode.FAIL_SEARCH_STUDY);
                }
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
        return studyRepository.findApplicants(studyId);
    }

    @Transactional(readOnly = true)
    public List<GoalsResponse> findGoals(Long studyId) {
        return studyGoalRepository.findGoalsById(studyId);
    }


    @Transactional(readOnly = true)
    public StudyInfoResponse getStudyInfo(Long studyId) {
        try {
            // goals 포함 조회
            Study studyWithGoals = studyRepository.findWithGoals(studyId)
                .orElseThrow(() -> new StudyDataException(ResponseCode.FAIL_GET_STUDY_INFO));

            // schedules 포함 조회
            Study studyWithSchedules = studyRepository.findWithStudySchedules(studyId)
                .orElseThrow(() -> new StudyDataException(ResponseCode.FAIL_GET_STUDY_INFO));

            // schedules 병합
            studyWithGoals.getSchedules().addAll(studyWithSchedules.getSchedules());
            int currentMemberCount = studyMemberRepository.countByStudy_StudyId(studyId);

            return StudyInfoResponse.fromEntity(studyWithGoals, currentMemberCount);
        } catch (StudyDataException e) {
            throw e; // 이미 처리된 예외는 다시 던지기
        } catch (Exception e) {
            // 그 외 예외는 공통 에러로 처리
            throw new StudyDataException(ResponseCode.FAIL_GET_STUDY_INFO);
        }
    }

    // 스터디 수정
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
            req.getExternalLink()
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

    @Transactional(readOnly = true)
    public boolean isUserStudyMember(Long memberId, Long studyId) {
        if (!studyRepository.existsById(studyId)) {
            throw new IllegalArgumentException("스터디가 존재하지 않습니다.");
        }

        return studyMemberRepository.existsByMember_IdAndStudy_StudyId(memberId, studyId);
    }

    // 스터디 멤버 조회
    @Transactional(readOnly = true)
    public List<StudyMemberResponse> getStudyMembers(Long studyId) {
        if (!studyRepository.existsById(studyId)) {
            throw new NotFoundException("스터디가 존재하지 않습니다.");
        }

        List<StudyMember> studyMembers = studyMemberRepository.findByStudyId(studyId);

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

    @Transactional
    public StudyCreationResponse createStudy(StudyCreationRequest req, Long memberId) {

        Member leader = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("회원 정보를 찾을 수 없습니다."));

        // 1. 스터디 생성
        Study study = Study.builder()
            .name(req.getName())
            .category(req.getCategory())
            .maxMembers(req.getMaxMembers())
            .region(req.getRegion())
            .place(req.getPlace())
            .isOnline(req.isOnline())
            .description(req.getDescription())
            .externalLink(req.getExternalLink())
            .studyType(req.getStudyType())
            .startDate(req.getStartDate())
            .endDate(req.getEndDate())
            .status(Status.READY)
            .activated(true)
            .createdAt(LocalDateTime.now())
            .build();

        // 2. 일정 추가 (String -> Enum 변환에 대한 예외 처리 포함)
        for (String dayStr : req.getSchedules()) {
            try {
                DayOfWeek day = DayOfWeek.valueOf(dayStr.toUpperCase());
                study.addSchedule(day, req.getStartTime(), req.getEndTime());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("올바르지 않은 요일 형식입니다: " + dayStr);
            }
        }

        // 3. 목표 추가(request 로 받은 goalId는 n주차 개념으로 오름차순 정렬)
        if (req.getGoals() != null) {
            req.getGoals().stream()
                .filter(g -> g.getContent() != null && !g.getContent().isBlank())
                .sorted(Comparator.comparingLong(g -> Optional.ofNullable(g.getGoalId()).orElse(Long.MAX_VALUE))) // goalId 오름차순
                .forEach(g -> {
                    StudyGoal goal = StudyGoal.builder()
                        .content(g.getContent())
                        .goalType(GoalType.WEEKLY)
                        .activated(true)
                        .study(study)
                        .build();
                    study.addGoal(goal);
                });
        }

        // 4. 저장
        studyRepository.save(study);

        // 5. 스터디장 등록
        StudyMember studyLeader = StudyMember.builder()
            .member(leader)
            .study(study)
            .studyRole(StudyRole.LEADER)
            .activated(true)
            .build();
        studyMemberRepository.save(studyLeader);

        return new StudyCreationResponse(study.getStudyId());
    }

    @Transactional
    public void applyToStudy(Long memberId, Long studyId, String introduction) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new NotFoundException("해당 스터디가 존재하지 않습니다."));

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("회원 정보를 찾을 수 없습니다."));


        // 이미 신청한 경우 예외 발생
        if (applicantRepository.existsByStudyAndMember(study, member)) {
            throw new IllegalStateException("이미 해당 스터디에 신청하셨습니다.");
        }

        Applicant applicant = Applicant.builder()
            .study(study)
            .member(member)
            .state(ApplicantState.WAIT) // 기본 신청 상태
            .introduction(introduction)
            .build();

        applicantRepository.save(applicant);
    }

    @Transactional(readOnly = true)
    public WeeklyGoalStatusResponse getWeeklyGoalStats(Long studyId, Long memberId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // 스터디 존재 여부 확인
        if (!studyRepository.existsById(studyId)) {
            throw new NotFoundException("스터디가 존재하지 않습니다.");
        }

        // memberId → studyMemberId 조회
        StudyMember studyMember = studyMemberRepository.findByStudyStudyIdAndMemberId(studyId, memberId)
            .orElseThrow(() -> new NotFoundException("스터디 멤버 정보를 찾을 수 없습니다."));
        Long studyMemberId = studyMember.getStudyMemberId();

        // 목표 전체에 대해 중복 없이 총 달성 카운트
        int totalCompletedCount = goalAchievementRepository.countTotalAchievements(
            studyId, studyMemberId, startDateTime, endDateTime
        );

        List<WeeklyGoalStatusResponse.GoalStat> goals = List.of(
            new WeeklyGoalStatusResponse.GoalStat(totalCompletedCount)
        );

        return new WeeklyGoalStatusResponse(
            studyId,
            startDate,
            endDate,
            goals
        );
    }

    public boolean isSurvival(Long studyId) {
        return (StudyType.SURVIVAL == studyRepository.findStudyType(studyId));
    }

    @Transactional
    public void updateStudyNotification(Long memberId, Long studyId, String notice) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND.message()));

        if(!studyMemberRepository.checkAcceptorHasRight(memberId, studyId)) {
            throw new HasNotRightException(ResponseCode.UNAUTHORIZED);
        }

        study.updateNotice(notice);
    }

    @Transactional(readOnly = true)
    public String findNotice(Long studyId) {
        if (!studyRepository.existsById(studyId)) {
            throw new IllegalArgumentException("스터디가 존재하지 않습니다.");
        }

        return studyRepository.findNotice(studyId)
            .orElse("공지사항이 없습니다.");
    }
}
