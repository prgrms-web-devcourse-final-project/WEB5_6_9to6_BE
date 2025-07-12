package com.grepp.spring.app.controller.api.study;

import com.grepp.spring.app.controller.api.study.payload.StudySearchRequest;
import com.grepp.spring.app.controller.api.study.payload.StudyUpdateRequest;
import com.grepp.spring.app.model.member.dto.response.ApplicantsResponse;
import com.grepp.spring.app.model.member.dto.response.AttendanceResponse;
import com.grepp.spring.app.model.member.dto.response.StudyMemberResponse;
import com.grepp.spring.app.model.member.entity.Attendance;
import com.grepp.spring.app.model.member.service.MemberService;
import com.grepp.spring.app.model.study.dto.StudyInfoResponse;
import com.grepp.spring.app.model.study.dto.StudyListResponse;
import com.grepp.spring.app.model.study.dto.WeeklyAttendanceResponse;
import com.grepp.spring.app.model.study.service.StudyService;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.util.SecurityUtil;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/studies", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudyController {

    private final MemberService memberService;
    private final StudyService studyService;

    // 출석체크
    @PostMapping("/{studyId}/attendance")
    public ResponseEntity<?> attendance(
        @PathVariable Long studyId,
        Authentication authentication
    ) {
        String email = authentication.getName();
        Long studyMemberId = memberService.findStudyMemberId(email, studyId);

        memberService.markAttendance(studyMemberId);

        return ResponseEntity.ok(CommonResponse.success("출석 체크 완료."));
    }

    // 주간 출석체크 조회(이번주)
// 주간 출석체크 조회(이번주)
    @GetMapping("/{studyId}/attendance")
    public ResponseEntity<?> weeklyAttendance(
        @PathVariable Long studyId,
        Authentication authentication
    ) {
        String email = authentication.getName();
        Long studyMemberId = memberService.findStudyMemberId(email, studyId);

        // 이번 주 출석 내역 조회
        List<Attendance> attendanceList = memberService.getWeeklyAttendanceEntities(studyMemberId);

        WeeklyAttendanceResponse response = new WeeklyAttendanceResponse(studyMemberId, attendanceList);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    // 스터디 목록(검색)
    @PostMapping("/search")
    public ResponseEntity<CommonResponse<List<StudyListResponse>>> searchStudies(
        @RequestBody StudySearchRequest req
    ) {
        List<StudyListResponse> responseList = studyService.searchStudiesWithMemberCount(req);
        return ResponseEntity.ok(CommonResponse.success(responseList));
    }

    // 스터디 정보 조회
    @GetMapping("/{studyId}")
    public ResponseEntity<?> getStudyInfo(@PathVariable Long studyId) {
        StudyInfoResponse data = studyService.getStudyInfo(studyId);
        return ResponseEntity.ok(CommonResponse.success(data));
    }

    // 스터디 정보 수정
    @PutMapping("/{studyId}")
    public ResponseEntity<?> updateStudyInfo(
        @PathVariable Long studyId,
        @RequestBody StudyUpdateRequest data
    ) {
        studyService.updateStudy(studyId, data);
        return ResponseEntity.ok(CommonResponse.noContent());
    }

    // 스터디 신청자 목록 조회
    @GetMapping("/{studyId}/applications")
    public ResponseEntity<?> getApplications(@PathVariable Long studyId) {
        List<ApplicantsResponse> applicants = studyService.getApplicants(studyId);
        return ResponseEntity.ok(CommonResponse.success(applicants));
    }

    // 스터디 신청 api 구현 필요(추후 추가 예정)

    // 유저가 스터디 맴버인지 조회
    @GetMapping("/{studyId}/members/me/check")
    public ResponseEntity<?> isMember(@PathVariable Long studyId) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        boolean isMember = studyService.isUserStudyMember(memberId, studyId);

        Map<String, Boolean> data = Map.of("isMember", isMember);

        return ResponseEntity.status(200).body(
            CommonResponse.success(data)
        );
    }

    // 스터디 맴버 리스트 조회
    @GetMapping("/{studyId}/members")
    public ResponseEntity<?> getMembers(@PathVariable Long studyId) {
        List<StudyMemberResponse> members = studyService.getStudyMembers(studyId);
        return ResponseEntity.ok(CommonResponse.success(members));
    }

    // 스터디 생성
    @PostMapping
    public ResponseEntity<?> createStudy(@RequestBody StudyCreationRequest req) {
        return ResponseEntity.status(200).body(
            CommonResponse.noContent()
        );
    }

    // 스터디 목표 조회
    @GetMapping("/{studyId}/goals")
    public ResponseEntity<?> getGoals(@PathVariable Long studyId) {
        return ResponseEntity.status(200).body(CommonResponse.success(studyService.findGoals(studyId)));
    }

    // 스터디 목표 달성
    @PostMapping("/{studyId}/goal/{goalId}")
    public ResponseEntity<?> successGoal(@PathVariable Long studyId, @PathVariable Long goalId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        log.info("memberId: {}", memberId);
        studyService.registGoal(List.of(studyId, memberId, goalId));
        return ResponseEntity.status(200).body(CommonResponse.noContent());
    }

    @Data
    @NoArgsConstructor
    private static class StudyCreationRequest {
        private String name;
        private StudyCategory category;
        private int maxMember;
        private Region region;
        private String place;
        private boolean isOnline;
        private List<StudySchedule> schedules;
        private LocalDate startDate;
        private LocalDate endDate;
        private String description;
        private String externalLink;
        private StudyType studyType;
        private List<Goal> goals;

        @Builder
        public StudyCreationRequest(String name, StudyCategory category, int maxMember,
            Region region,
            String place, List<StudySchedule> schedules, LocalDate startDate,
            LocalDate endDate,
            String description, String externalLink, StudyType studyType,
            List<Goal> goals) {
            this.name = name;
            this.category = category;
            this.maxMember = maxMember;
            this.region = region;
            this.place = place;
            this.isOnline = (region == Region.ONLINE) ? true:false;
            this.schedules = schedules;
            this.startDate = startDate;
            this.endDate = endDate;
            this.description = description;
            this.externalLink = externalLink;
            this.studyType = studyType;
            this.goals = goals;
        }
    }

    @Data
    @NoArgsConstructor
    private static class StudySchedule {
        DayOfWeek dayOfWeek;
        LocalTime startTime;
        LocalTime endTime;

        @Builder
        public StudySchedule(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
            this.dayOfWeek = dayOfWeek;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    @Data
    @NoArgsConstructor
    private static class Goal {
        long goalId;
        String content;
        DayOfWeek checkDay;

        @Builder
        public Goal(long goalId, String content, DayOfWeek checkDay) {
            this.goalId = goalId;
            this.content = content;
            this.checkDay = checkDay;
        }
    }

    // 스터디 타입
    private enum StudyType {
        DEFAULT, SURVIVAL
    }

    // 스터디 주제
    private enum StudyCategory {
        LANGUAGE, JOB, PROGRAMMING, EXAM_PUBLIC, EXAM_SCHOOL, OTHERS
    }

    // 활동 지역
    private enum Region {
        ONLINE, SEOUL, JEJU;
    }


    // 진행 요일
    private enum DayOfWeek {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }

}
