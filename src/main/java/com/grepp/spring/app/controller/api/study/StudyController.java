package com.grepp.spring.app.controller.api.study;

import com.grepp.spring.app.controller.api.study.payload.ApplicationRequest;
import com.grepp.spring.app.controller.api.study.payload.StudyCreationRequest;
import com.grepp.spring.app.controller.api.study.payload.StudySearchRequest;
import com.grepp.spring.app.controller.api.study.payload.StudyUpdateRequest;
import com.grepp.spring.app.model.chat.service.ChatService;
import com.grepp.spring.app.model.member.dto.response.ApplicantsResponse;
import com.grepp.spring.app.model.member.dto.response.AttendanceResponse;
import com.grepp.spring.app.model.member.dto.response.StudyMemberResponse;
import com.grepp.spring.app.model.member.entity.Attendance;
import com.grepp.spring.app.model.member.service.MemberService;
import com.grepp.spring.app.model.study.dto.StudyInfoResponse;
import com.grepp.spring.app.model.study.dto.StudyListResponse;
import com.grepp.spring.app.model.study.dto.WeeklyAttendanceResponse;
import com.grepp.spring.app.model.study.dto.WeeklyGoalStatusResponse;
import com.grepp.spring.app.model.study.entity.Study;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/studies", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudyController {

    private final MemberService memberService;
    private final StudyService studyService;
    private final ChatService chatService;

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
    @GetMapping("/{studyId}/applications-list")
    public ResponseEntity<?> getApplications(@PathVariable Long studyId) {
        List<ApplicantsResponse> applicants = studyService.getApplicants(studyId);
        return ResponseEntity.ok(CommonResponse.success(applicants));
    }

    // 스터디 신청
    @PostMapping("/{studyId}/application")
    public ResponseEntity<?> application(
        @PathVariable Long studyId,
        @RequestBody ApplicationRequest req
    ) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        studyService.applyToStudy(memberId, studyId, req.getIntroduction());
        return ResponseEntity.ok(CommonResponse.success(""));
    }

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
        // 1. 서비스에서 스터디 생성 수행
        Study study = studyService.createStudy(req);
        // 2. 채팅방 생성
        chatService.createChatRoom(study);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CommonResponse.success("스터디가 성공적으로 생성되었습니다."));
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

    // 스터디 목표 달성 여부 조회
    @GetMapping("/{studyId}/goals/completed")
    public ResponseEntity<?> getWeeklyGoalStats(
        @PathVariable Long studyId,
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Long memberId = SecurityUtil.getCurrentMemberId(); // 로그인된 사용자
        WeeklyGoalStatusResponse response = studyService.getWeeklyGoalStats(studyId, memberId, endDate);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

}
