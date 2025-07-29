package com.grepp.spring.app.model.study.service;

import com.grepp.spring.app.controller.api.alarm.payload.AlarmRequest;
import com.grepp.spring.app.controller.api.study.payload.ApplicationResultRequest;
import com.grepp.spring.app.controller.api.study.payload.StudyCreationRequest;
import com.grepp.spring.app.controller.api.study.payload.StudySearchRequest;
import com.grepp.spring.app.controller.api.study.payload.StudyUpdateRequest;
import com.grepp.spring.app.controller.api.study.payload.WeeklyAchievementCount;
import com.grepp.spring.app.model.alarm.code.AlarmType;
import com.grepp.spring.app.model.alarm.service.AlarmService;
import com.grepp.spring.app.model.auth.code.Role;
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
import com.grepp.spring.infra.error.exceptions.AlreadyExistException;
import com.grepp.spring.infra.error.exceptions.EarlierDateException;
import com.grepp.spring.infra.error.exceptions.HasNotRightException;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import com.grepp.spring.infra.error.exceptions.StudyDataException;
import com.grepp.spring.infra.response.ResponseCode;
import com.grepp.spring.infra.util.SecurityUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyGoalRepository studyGoalRepository;
    private final GoalAchievementRepository goalAchievementRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final MemberRepository memberRepository;
    private final ApplicantRepository applicantRepository;
    private final AlarmService alarmService;
    private final StudyMemberService studyMemberService;
    private final ApplicantService applicantService;

    //필터 조건에 따라 스터디 목록 + 현재 인원 수 조회
//    @Transactional(readOnly = true)
//    public List<StudyListResponse> searchStudiesWithMemberCount(StudySearchRequest req) {
//        if (req == null) {
//            throw new StudyDataException(ResponseCode.BAD_REQUEST);
//        }
//
//        Page<Study> studies;
//        try {
//            studies = studyRepository.searchStudiesPage(req, req.getPageable());
//        } catch (Exception e) {
//            throw new StudyDataException(ResponseCode.FAIL_SEARCH_STUDY);
//        }
//
//        return studies.getContent().stream()
//            .map(study -> {
//                if (study.getStudyId() == null) {
//                    throw new StudyDataException(ResponseCode.FAIL_SEARCH_STUDY);
//                }
//
//                int currentMemberCount;
//                try {
//                    currentMemberCount = studyMemberRepository.countByStudy_StudyId(study.getStudyId());
//                } catch (Exception e) {
//                    throw new StudyDataException(ResponseCode.FAIL_SEARCH_STUDY);
//                }
//
//                try {
//                    return StudyListResponse.fromEntity(study, currentMemberCount);
//                } catch (Exception e) {
//                    throw new StudyDataException(ResponseCode.FAIL_SEARCH_STUDY);
//                }
//            })
//            .collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public List<StudyListResponse> searchStudiesWithMemberCount(StudySearchRequest req) {
        if (req == null) {
            throw new StudyDataException(ResponseCode.BAD_REQUEST);
        }

        Page<StudyListResponse> pageResult = studyRepository.searchStudiesWithMemberCount(req, req.getPageable());

        return pageResult.getContent();
    }


    @Transactional
    public void registGoal(List<Long> ids) {
        Long studyId = ids.get(0);
        Long memberId = ids.get(1);
        Long goalId = ids.get(2);

        if (!studyRepository.existsByStudyIdAndActivatedTrue(studyId)) {
            throw new NotFoundException("존재하지 않거나 비활성화된 스터디입니다.");
        }

        // 스터디 시작 전
        LocalDate studyStartDate = studyRepository.findStudyStartDate(studyId);
        if (studyStartDate.isAfter(LocalDate.now())) {
            throw new EarlierDateException(ResponseCode.BAD_REQUEST);
        }

        StudyGoal studyGoal = studyGoalRepository.findById(goalId)
            .orElseThrow(() -> new NotFoundException("해당 목표를 찾을 수 없습니다."));
        StudyMember studyMember = studyMemberRepository.findByStudyIdAndMemberId(studyId, memberId)
            .orElseThrow(() -> new NotFoundException("스터디 멤버 정보를 찾을 수 없습니다."));

        if(goalAchievementRepository.findSameLog(goalId, memberId, LocalDate.now())) {
            throw new AlreadyExistException(ResponseCode.ALREADY_EXIST);
        }

        GoalAchievement newAchievement = GoalAchievement.builder()
            .studyGoal(studyGoal)
            .studyMember(studyMember)
            .isAccomplished(true)
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
        if (!studyRepository.existsByStudyIdAndActivatedTrue(studyId)) {
            throw new NotFoundException("존재하지 않거나 비활성화된 스터디입니다.");
        }
        return studyGoalRepository.findStudyGoals(studyId);
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
            int currentMemberCount = studyMemberRepository.countByStudyId(studyId);

            return StudyInfoResponse.fromEntity(studyWithGoals, currentMemberCount);
        } catch (StudyDataException e) {
            throw e; // 이미 처리된 예외는 다시 던지기
        } catch (Exception e) {
            // 그 외 예외는 공통 에러로 처리
            throw new StudyDataException(ResponseCode.FAIL_GET_STUDY_INFO);
        }
    }

    @Transactional
    public void updateStudy(Long studyId, StudyUpdateRequest req) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));
        Long memberId = SecurityUtil.getCurrentMemberId();

        if (!Boolean.TRUE.equals(studyMemberRepository.checkAcceptorHasRight(memberId, studyId))) {
            throw new HasNotRightException(ResponseCode.UNAUTHORIZED);
        }

        // 1. 스터디 기본 정보 업데이트
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

        // 2. 스터디 일정 업데이트 (기존 제거 후 추가)
        study.getSchedules().clear();
        for (DayOfWeek day : req.getSchedules()) {
            study.addSchedule(day, req.getStartTime(), req.getEndTime());
        }

        // 3-1. 목표 수정/삭제: goalId가 있는 것만 매핑
        Map<Long, StudyUpdateRequest.GoalUpdateDTO> goalUpdateMap = req.getGoals().stream()
            .filter(g -> g.getGoalId() != null)
            .collect(Collectors.toMap(StudyUpdateRequest.GoalUpdateDTO::getGoalId, g -> g));

        // 3-2. 기존 목표 목록에서 수정 또는 삭제
        Iterator<StudyGoal> iterator = study.getGoals().iterator();
        while (iterator.hasNext()) {
            StudyGoal goal = iterator.next();
            StudyUpdateRequest.GoalUpdateDTO dto = goalUpdateMap.get(goal.getGoalId());

            if (dto != null) {
                // 수정
                goal.update(dto.getContent(), dto.getType());
            } else {
                // 삭제
                iterator.remove();      // JPA에서 인식되도록 원본 리스트에서 직접 제거
                goal.setStudy(null);    // 연관관계 정리
            }
        }

        // 3-3. 추가: goalId가 없는 새로운 목표들
        req.getGoals().stream()
            .filter(g -> g.getGoalId() == null)
            .forEach(g -> study.addGoal(g.getContent(), g.getType()));
    }



    @Transactional(readOnly = true)
    public boolean isUserStudyMember(Long memberId, Long studyId) {
        if (!studyRepository.existsByStudyIdAndActivatedTrue(studyId)) {
            throw new IllegalArgumentException("존재하지 않거나 비활성화된 스터디입니다.");
        }

        return studyMemberRepository.existStudyMember(memberId, studyId);
    }

    // 스터디 멤버 조회
    @Transactional(readOnly = true)
    public List<StudyMemberResponse> getStudyMembers(Long studyId) {
        // 활성화된 스터디만 조회 가능
        if (!studyRepository.existsByStudyIdAndActivatedTrue(studyId)) {
            throw new NotFoundException("스터디가 존재하지 않거나 비활성화 상태입니다.");
        }

        List<StudyMember> studyMembers = studyMemberRepository.findByStudyId(studyId);

        return studyMembers.stream()
            .filter(sm -> sm.getMember().getRole() != Role.ROLE_ADMIN)
            .map(studyMember -> {
                Member member = studyMember.getMember();
                return StudyMemberResponse.builder()
                    .studyMemberId(studyMember.getStudyMemberId())
                    .memberId(member.getId())
                    .nickname(member.getNickname())
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

        if(req.getStudyType().equals(StudyType.SURVIVAL) && memberRepository.findRole(memberId) == Role.ROLE_USER) {
            throw new HasNotRightException(ResponseCode.UNAUTHORIZED);
        }

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
            .build();
        studyMemberRepository.save(studyLeader);

        return new StudyCreationResponse(study.getStudyId());
    }

    // 스터디 신청(+ 알림)
    @Transactional
    public void applyToStudy(Long memberId, Long studyId, String introduction) {
        Study study = studyRepository.findByStudyIdAndActivatedTrue(studyId)
            .orElseThrow(() -> new NotFoundException("존재하지 않거나 비활성화된 스터디입니다."));

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("회원 정보를 찾을 수 없습니다."));

        // 이미 신청한 경우 예외 발생
        if (applicantRepository.existsByStudyAndMember(study, member)) {
            throw new IllegalStateException("이미 해당 스터디에 신청하셨습니다.");
        }

        //  이미 가입한 경우 예외 발생
        if (studyMemberRepository.existStudyMember(member.getId(), study.getStudyId())) {
            throw new IllegalStateException("이미 해당 스터디에 가입되어 있습니다.");
        }

        // 서바이벌 스터디는 바로 가입 처리
        if (study.getStudyType() == StudyType.SURVIVAL) {
            StudyMember studyMember = StudyMember.builder()
                .study(study)
                .member(member)
                .studyRole(StudyRole.MEMBER)
                .activated(true)
                .build();

            studyMemberRepository.save(studyMember);

            return;
        }

        Applicant applicant = Applicant.builder()
            .study(study)
            .member(member)
            .state(ApplicantState.WAIT) // 기본 신청 상태
            .introduction(introduction)
            .build();

        applicantRepository.save(applicant);

        // 신청 알림 스터디장에게 전송
        StudyMember leader = studyMemberRepository.findByStudyAndStudyRole(study, StudyRole.LEADER)
            .orElseThrow(() -> new NotFoundException("스터디장 정보를 찾을 수 없습니다."));

        Member leaderMember = leader.getMember();

        alarmService.createAndSendAlarm(AlarmRequest.builder()
            .senderId(memberId)  // 신청자
            .receiverId(leaderMember.getId())  // 스터디장
            .message(member.getNickname() + "님이 " + study.getName() + " 에 가입요청을 보냈습니다.")
            .type(AlarmType.APPLY)
            .studyId(studyId)
            .build());

    }

    // 서바이벌 스터디 판별
    @Transactional(readOnly = true)
    public boolean isSurvival(Long studyId) {
        if (!studyRepository.existsByStudyIdAndActivatedTrue(studyId)) {
            throw new NotFoundException("존재하지 않거나 비활성화된 스터디입니다.");
        }

        return (StudyType.SURVIVAL == studyRepository.findStudyType(studyId));
    }

    // 스터디 신청 결과(승인/거절 + 알림)
    @Transactional
    public void processApplicationResult(Long senderId, Long studyId, ApplicationResultRequest req) {

        boolean isSurvival = isSurvival(studyId);
        Long receiverId = req.getMemberId();
        ApplicantState result = req.getApplicationResult();

        if (!isSurvival) {
            // 스터디 이름 조회 (알림 메시지용)
            Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new NotFoundException("스터디를 찾을 수 없습니다."));
            String studyName = study.getName();

            // 신청 상태 업데이트
            applicantService.updateState(senderId, receiverId, studyId, result);

            // 승인 시 멤버로 등록
            if (result == ApplicantState.ACCEPT) {
                studyMemberService.saveMember(studyId, receiverId);
            }

            // 결과 메시지 생성
            String message = switch (result) {
                case ACCEPT -> String.format("스터디 [%s] 가입신청이 승인되었습니다.", studyName);
                case REJECT -> String.format("스터디 [%s] 가입신청이 거절되었습니다.", studyName);
                default -> throw new IllegalArgumentException("잘못된 신청 결과입니다.");
            };

            // 알림 전송
            alarmService.createAndSendAlarm(
                AlarmRequest.builder()
                    .senderId(senderId)
                    .receiverId(receiverId)
                    .studyId(studyId)
                    .type(AlarmType.RESULT)
                    .resultStatus(result)
                    .message(message)
                    .build()
            );
        } else {
            // 서바이벌 스터디는 자동 승인
            studyMemberService.applyToStudy(receiverId, studyId);
        }
    }

    @Transactional(readOnly = true)
    public WeeklyGoalStatusResponse getWeeklyGoalStats(Long studyId, Long memberId) {
        // studyStartDate
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new NotFoundException("스터디가 존재하지 않습니다."));
        LocalDate studyStartDate = study.getStartDate();

        // startDate = 스터디 생성일
        LocalDate startDate = studyStartDate;
        LocalDate endDate = LocalDate.now(); // endDate는 현재

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // 활성화된 스터디만 허용
        if (!studyRepository.existsByStudyIdAndActivatedTrue(studyId)) {
            throw new NotFoundException("존재하지 않거나 비활성화된 스터디입니다.");
        }

        // memberId → studyMemberId 조회
        StudyMember studyMember = studyMemberRepository.findByStudyIdAndMemberId(studyId, memberId)
            .orElseThrow(() -> new NotFoundException("스터디 멤버 정보를 찾을 수 없습니다."));
        Long studyMemberId = studyMember.getStudyMemberId();

        List<WeeklyAchievementCount> weeklyCounts = goalAchievementRepository.countWeeklyAchievements(
            studyId, studyMemberId, startDateTime, endDateTime
        );

        Map<Integer, Long> countsMap = weeklyCounts.stream()
            .collect(Collectors.toMap(
                dto -> Integer.parseInt(dto.getWeek()),
                WeeklyAchievementCount::getCount
            ));

        // 스터디 시작일을 기준으로 주차계산
        int startWeek = (int) (ChronoUnit.DAYS.between(studyStartDate, startDate) / 7) + 1;
        int endWeek = (int) (ChronoUnit.DAYS.between(studyStartDate, endDate) / 7) + 1;

        // 전체 주차를 순회, 없으면 0
        List<WeeklyGoalStatusResponse.GoalStat> goals = IntStream.rangeClosed(startWeek, endWeek)
            .mapToObj(week -> {
                long count = countsMap.getOrDefault(week, 0L);
                return new WeeklyGoalStatusResponse.GoalStat(String.valueOf(week), count);
            })
            .collect(Collectors.toList());

        return new WeeklyGoalStatusResponse(
            studyId,
            goals
        );
    }

    @Transactional
    public void updateStudyNotification(Long memberId, Long studyId, String notice) {
        // 비활성 스터디 접근 방지
        Study study = studyRepository.findByStudyIdAndActivatedTrue(studyId)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND.message()));

        if (!Boolean.TRUE.equals(studyMemberRepository.checkAcceptorHasRight(memberId, studyId))) {
            throw new HasNotRightException(ResponseCode.UNAUTHORIZED);
        }

        study.updateNotice(notice);
    }

    @Transactional(readOnly = true)
    public String findNotice(Long studyId) {
        if (!studyRepository.existsByStudyIdAndActivatedTrue(studyId)) {
            throw new IllegalArgumentException("존재하지 않거나 비활성화된 스터디입니다.");
        }

        return studyRepository.findNotice(studyId)
            .orElse("공지사항이 없습니다.");
    }

    // 매일 자정(00:00:00)에 종료일이 지난 스터디를 일괄 삭제
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void deactivateExpiredStudies() {
        LocalDate today = LocalDate.now();
        List<Study> expiredStudies = studyRepository.findAllByEndDateBefore(today);

        if (expiredStudies.isEmpty()) {
            log.info("만료된 스터디 없음 - 비활성화 생략");
            return;
        }

        log.info("만료된 스터디 {}개 비활성화 시작...", expiredStudies.size());

        for (Study study : expiredStudies) {
            if (study.isActivated()) {
                study.setActivated(false);
            }
        }

        log.info("만료된 스터디 비활성화 완료");
    }
}
