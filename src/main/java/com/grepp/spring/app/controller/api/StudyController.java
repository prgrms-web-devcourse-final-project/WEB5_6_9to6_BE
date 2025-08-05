package com.grepp.spring.app.controller.api;

import com.grepp.spring.app.model.study.dto.request.ApplicationRequest;
import com.grepp.spring.app.model.study.dto.request.ApplicationResultRequest;
import com.grepp.spring.app.model.study.dto.response.CheckGoalResponse;
import com.grepp.spring.app.model.study.dto.request.NotificationUpdateRequest;
import com.grepp.spring.app.model.study.dto.request.StudyCreationRequest;
import com.grepp.spring.app.model.study.dto.request.StudySearchRequest;
import com.grepp.spring.app.model.study.dto.request.StudyUpdateRequest;
import com.grepp.spring.app.model.chat.service.ChatService;
import com.grepp.spring.app.model.member.dto.response.ApplicantsResponse;
import com.grepp.spring.app.model.member.dto.response.StudyMemberResponse;
import com.grepp.spring.app.model.studyMmeber.entity.Attendance;
import com.grepp.spring.app.model.member.service.MemberService;
import com.grepp.spring.app.model.study.code.Category;
import com.grepp.spring.app.model.study.code.Status;
import com.grepp.spring.app.model.study.dto.response.StudyCreationResponse;
import com.grepp.spring.app.model.study.dto.response.StudyInfoResponse;
import com.grepp.spring.app.model.study.dto.response.StudyListResponse;
import com.grepp.spring.app.model.study.dto.response.WeeklyAttendanceResponse;
import com.grepp.spring.app.model.study.dto.response.WeeklyGoalStatusResponse;
import com.grepp.spring.app.model.study.dto.response.GoalsResponse;
import com.grepp.spring.app.model.study.dto.response.StudyNoticeResponse;
import com.grepp.spring.app.model.study.service.ApplicantService;
import com.grepp.spring.app.model.studyMmeber.serivce.StudyMemberService;
import com.grepp.spring.app.model.study.service.StudyService;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "스터디 API", description = "스터디 생성, 조회, 가입, 관리 등 스터디 관련 API 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/studies", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudyController {

    private final MemberService memberService;
    private final StudyService studyService;
    private final ChatService chatService;
    private final ApplicantService applicantService;
    private final StudyMemberService studyMemberService;

    // 카테고리 & statuses 조회(enum)
    @Operation(summary = "스터디 카테고리 및 상태 목록 조회",
        description = "스터디 생성 및 검색에 사용되는 카테고리(Enum)와 상태(Enum)의 전체 목록을 문자열 리스트로 조회합니다.")
    @GetMapping("/categories")
    public ResponseEntity<CommonResponse<Map<String, Object>>> getCategories() {
        Map<String, Object> data = Map.of(
            "categories", Arrays.stream(Category.values()).map(Enum::name).toList(),
            "statuses", Arrays.stream(Status.values()).map(Enum::name).toList()
        );
        return ResponseEntity.ok(CommonResponse.success(data));
    }

    // 출석체크
    @Operation(summary = "스터디 출석 체크", description = """
        현재 로그인한 사용자가 특정 스터디(`studyId`)에 대해 출석 체크를 합니다.
        - 이 API는 인증이 필요하며, 요청 헤더에 유효한 토큰이 있어야 합니다.
        """)
    @PostMapping("/{studyId}/attendance")
    public ResponseEntity<CommonResponse<String>> attendance(
        @PathVariable Long studyId,
        Authentication authentication
    ) {
        String email = authentication.getName();
        Long studyMemberId = memberService.findStudyMemberId(email, studyId);
        Long memberId = SecurityUtil.getCurrentMemberId();

        memberService.markAttendance(studyMemberId);
        // 100 point 지급 받음
        memberService.addRewardPoints(memberId);

        return ResponseEntity.ok(CommonResponse.success("출석 체크 완료."));
    }

    // 주간 출석체크 조회(이번주)
    @Operation(
        summary = "주간 출석 내역 조회 (이번 주)",
        description = """
        로그인한 사용자의 특정 스터디(`studyId`)에 대한 이번 주 출석 정보를 조회합니다.

        - 인증된 사용자만 사용할 수 있습니다. (`Authorization` 헤더에 JWT 토큰 필요)
        - `memberId`를 쿼리 파라미터로 전달하지 않으면, 로그인한 사용자의 정보로 조회합니다.
        - 응답에는 스터디 멤버 ID와 이번 주 출석 현황이 포함됩니다.
        """
    )
    @GetMapping("/{studyId}/attendance")
    public ResponseEntity<CommonResponse<WeeklyAttendanceResponse>> weeklyAttendance(
        @PathVariable Long studyId,
        @RequestParam(required = false) Long memberId,
        Authentication authentication
    ) {
        if (memberId == null) {
            String email = authentication.getName();
            memberId = memberService.findMemberIdByEmail(email); // 이 메서드도 필요함
        }

        Long studyMemberId = memberService.findStudyMemberId(memberId, studyId);
        List<Attendance> attendanceList = memberService.getWeeklyAttendanceEntities(studyMemberId);

        WeeklyAttendanceResponse response = new WeeklyAttendanceResponse(studyMemberId, attendanceList);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    // 스터디 목록(검색)
    @Operation(summary = "스터디 목록 검색", description = """
        요청 body에 `StudySearchRequest`를 포함해야합니다.
        - 카테고리, 스터디 상태, 검색어 등 다양한 조건으로 스터디를 검색하고, 목록을 반환합니다.
        """)
    @PostMapping("/search")
    public ResponseEntity<CommonResponse<List<StudyListResponse>>> searchStudies(
        @Valid @RequestBody StudySearchRequest req
    ) {
        List<StudyListResponse> responseList = studyService.searchStudiesWithMemberCount(req);
        return ResponseEntity.ok(CommonResponse.success(responseList));
    }

    // 스터디 정보 조회
    @Operation(summary = "스터디 상세 정보 조회", description = "스터디 ID(`studyId`)를 이용하여 스터디의 상세 정보를 조회합니다.")
    @GetMapping("/{studyId}")
    public ResponseEntity<CommonResponse<StudyInfoResponse>> getStudyInfo(@PathVariable Long studyId) {
        StudyInfoResponse data = studyService.getStudyInfo(studyId);
        return ResponseEntity.ok(CommonResponse.success(data));
    }

    // 스터디 정보 수정
    @Operation(summary = "스터디 정보 수정", description = """
        요청 body에 `StudyUpdateRequest`를 포함해야합니다.
        - 스터디 ID(`studyId`)에 해당하는 스터디의 정보를 수정합니다. 
        - 스터디장만 호출 가능합니다.
        """)
    @PutMapping("/{studyId}")
    public ResponseEntity<CommonResponse<Void>> updateStudyInfo(
        @PathVariable Long studyId,
        @RequestBody StudyUpdateRequest data
    ) {
        studyService.updateStudy(studyId, data);
        return ResponseEntity.ok(CommonResponse.noContent());
    }

    // 스터디 신청자 목록 조회
    @Operation(summary = "스터디 신청자 목록 조회", description = "스터디 ID(`studyId`)에 해당하는 스터디의 가입 신청자 목록을 조회합니다.")
    @GetMapping("/{studyId}/applications-list")
    public ResponseEntity<CommonResponse<List<ApplicantsResponse>>> getApplications(@PathVariable Long studyId) {
        List<ApplicantsResponse> applicants = studyService.getApplicants(studyId);
        return ResponseEntity.ok(CommonResponse.success(applicants));
    }

    // 스터디 신청
    @Operation(summary = "스터디 가입 신청", description = """
        요청 body에 `ApplicationRequest`를 포함해야합니다.
        - 현재 로그인한 사용자가 특정 스터디(`studyId`)에 가입 신청을 합니다. 자기소개를 포함할 수 있습니다.
        - 이 API는 인증이 필요하며, 요청 헤더에 유효한 토큰이 있어야 합니다.
        """)
    @PostMapping("/{studyId}/application")
    public ResponseEntity<CommonResponse<String>> application(
        @PathVariable Long studyId,
        @RequestBody ApplicationRequest req
    ) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        studyService.applyToStudy(memberId, studyId, req.getIntroduction());
        return ResponseEntity.ok(CommonResponse.success(""));
    }

    // 유저가 스터디 맴버인지 조회
    @Operation(summary = "현재 사용자의 스터디 멤버 여부 확인", description = """
        현재 로그인한 사용자가 특정 스터디(`studyId`)의 멤버인지 여부를 확인합니다.
        - `isMember` 필드가 `true` 또는 `false`로 반환됩니다.
        - 이 API는 인증이 필요하며, 요청 헤더에 유효한 토큰이 있어야 합니다.
        """)
    @GetMapping("/{studyId}/members/me/check")
    public ResponseEntity<CommonResponse<Map<String, Boolean>>> isMember(@PathVariable Long studyId) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        boolean isMember = studyService.isUserStudyMember(memberId, studyId);

        Map<String, Boolean> data = Map.of("isMember", isMember);

        return ResponseEntity.status(200).body(
            CommonResponse.success(data)
        );
    }

    // 스터디 맴버 리스트 조회
    @Operation(summary = "스터디 멤버 목록 조회", description = "스터디 ID(`studyId`)에 해당하는 스터디에 속한 모든 멤버의 목록을 조회합니다.")
    @GetMapping("/{studyId}/members")
    public ResponseEntity<CommonResponse<List<StudyMemberResponse>>> getMembers(@PathVariable Long studyId) {
        List<StudyMemberResponse> members = studyService.getStudyMembers(studyId);
        return ResponseEntity.ok(CommonResponse.success(members));
    }

    // 스터디 생성
    @Operation(summary = "스터디 생성", description = """
        요청 body에 `StudyCreationRequest`를 포함해야합니다.
        - 새로운 스터디를 생성합니다. 생성과 동시에 해당 스터디의 채팅방도 함께 생성됩니다.
        """)
    @PostMapping
    public ResponseEntity<CommonResponse<StudyCreationResponse>> createStudy(@RequestBody StudyCreationRequest req) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        StudyCreationResponse response = studyService.createStudy(req, memberId);

        chatService.createChatRoom(response.getStudyId());

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CommonResponse.success(response));
    }


    // 스터디 목표 조회
    @Operation(summary = "스터디 목표 목록 조회", description = "스터디 ID(`studyId`)에 설정된 목표 목록을 조회합니다.")
    @GetMapping("/{studyId}/goals")
    public ResponseEntity<CommonResponse<List<GoalsResponse>>> getGoals(@PathVariable Long studyId) {
        return ResponseEntity.status(200).body(CommonResponse.success(studyService.findGoals(studyId)));
    }

    // 스터디 목표 달성
    @Operation(summary = "스터디 목표 달성 등록", description = """
        현재 로그인한 사용자가 특정 스터디(`studyId`)의 특정 목표(`goalId`)를 달성했음을 등록합니다.
        - 이 API는 인증이 필요하며, 요청 헤더에 유효한 토큰이 있어야 합니다.
        """)
    @PostMapping("{studyId}/goal/{goalId}")
    public ResponseEntity<CommonResponse<Void>> successGoal(@PathVariable Long studyId, @PathVariable Long goalId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        log.info("memberId: {}", memberId);
        studyService.registGoal(List.of(studyId, memberId, goalId));
        return ResponseEntity.status(200).body(CommonResponse.noContent());
    }

    @Operation(
        summary = "주간 스터디 목표 달성 현황 조회",
        description = """
        특정 사용자의 특정 스터디(`studyId`)에 대한 이번 주 목표 달성 현황을 조회합니다.

        - `memberId`를 요청 파라미터로 전달하지 않으면, 현재 로그인한 사용자의 ID를 기반으로 조회합니다.
        - 인증이 필요하며, `Authorization` 헤더에 유효한 JWT 토큰이 있어야 합니다.
        """
    )
    @GetMapping("/{studyId}/goals/completed")
    public ResponseEntity<CommonResponse<WeeklyGoalStatusResponse>> getWeeklyGoalStats(
        @PathVariable Long studyId,
        @RequestParam(required = false) Long memberId
    ) {
        if (memberId == null) {
            memberId = SecurityUtil.getCurrentMemberId(); // 로그인된 사용자 ID
        }

        WeeklyGoalStatusResponse response = studyService.getWeeklyGoalStats(studyId, memberId);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    // 스터디 가입 승인, 거절
    @Operation(
        summary = "스터디 가입 승인, 거절",
        description = """
        요청 body에 `ApplicationResultRequest`를 포함해야합니다.
        스터디 가입 신청에 대해 승인 또는 거절을 처리합니다.
        - 서바이벌 스터디인 경우 바로 신청이 됩니다.
        - 요청 body에 `memberId`와 `applicationResult`(ACCEPT, REJECT)를 포함해야 합니다.
        - **승인(ACCEPT)** 시 신청자는 스터디 멤버로 추가됩니다.
        - **거절(REJECT)** 시 신청자의 상태만 업데이트됩니다.
        - 일반 스터디에 한해 스터디장만 호출 가능합니다.
        """
    )
    @PostMapping("/{studyId}/applications/respond")
    public ResponseEntity<CommonResponse<Void>> responseStudyApplication(
        @PathVariable Long studyId,
        @RequestBody ApplicationResultRequest req) {

        Long senderId = SecurityUtil.getCurrentMemberId();
        studyService.processApplicationResult(senderId, studyId, req);

        return ResponseEntity.ok(CommonResponse.noContent());
    }

// 수정했는데 혹시 문제 생길까봐 기존 코드 주석 처리
//    @PostMapping("/{studyId}/applications/respond")
//    public ResponseEntity<CommonResponse<Void>> responseStudyApplication(
//        @PathVariable Long studyId,
//        @RequestBody ApplicationResultRequest req) {
//
//        boolean isSurvival = studyService.isSurvival(studyId);
//
//        // 신청자 상태 변경
//        if(!isSurvival) {
//            Long acceptorId = SecurityUtil.getCurrentMemberId();
//            applicantService.updateState(acceptorId, req.getMemberId(), studyId, req.getApplicationResult());
//
//            // 스터디 멤버에 저장
//            if (req.getApplicationResult() == ApplicantState.ACCEPT) {
//                studyMemberService.saveMember(studyId, req.getMemberId());
//            }
//        }
//        else {
//            studyMemberService.applyToStudy(req.getMemberId(), studyId);
//        }
//
//        return ResponseEntity.ok(CommonResponse.noContent());
//    }

    @Operation(summary = "스터디 공지사항 수정", description = """
        요청 body에 `NotificationUpdateRequest`를 포함해야합니다.
        - 특정 스터디(`studyId`)의 공지사항을 수정합니다.
        - 스터디장만 호출 가능합니다.
        - 이 API는 인증이 필요하며, 요청 헤더에 유효한 토큰이 있어야 합니다.
        """)
    @PatchMapping("/{studyId}/notification")
    public ResponseEntity<CommonResponse<Void>> updateStudyNotification(
        @PathVariable Long studyId,
        @RequestBody NotificationUpdateRequest req
    ) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        studyService.updateStudyNotification(memberId, studyId, req.getNotice());

        return ResponseEntity.ok(CommonResponse.noContent());
    }

    @Operation(summary = "스터디 공지사항 조회",
        description = "특정 스터디(`studyId`)의 공지사항을 조회합니다.")
    @GetMapping("/{studyId}/notification")
    public ResponseEntity<CommonResponse<StudyNoticeResponse>> getStudyNotification(
        @PathVariable Long studyId
    ) {
        StudyNoticeResponse res = new StudyNoticeResponse(studyService.findNotice(studyId));
        return ResponseEntity.ok(CommonResponse.success(res));
    }

    // 맴버의 특정 스터디에 대한 목표 달성여부 체크
    @Operation(summary = "스터디 목표 달성 여부 조회", description = """
        현재 로그인한 사용자의 특정 스터디(`studyId`)에 대한 모든 목표의 달성 여부를 조회합니다.
        - 각 목표별로 달성했는지(`isAchieved`) 여부를 리스트로 반환합니다.
        - 이 API는 인증이 필요하며, 요청 헤더에 유효한 토큰이 있어야 합니다.
        """)
    @GetMapping("/{studyId}/check-goal")
    public ResponseEntity<CommonResponse<List<CheckGoalResponse>>> getCheckGoal(
        @PathVariable Long studyId
    ){
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<CheckGoalResponse> res = studyMemberService.getGoalStatuses(studyId, memberId);
        return ResponseEntity.ok(CommonResponse.success(res));
    }
}