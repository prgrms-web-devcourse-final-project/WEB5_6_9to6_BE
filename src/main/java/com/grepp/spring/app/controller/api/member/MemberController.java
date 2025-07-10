package com.grepp.spring.app.controller.api.member;

import com.grepp.spring.app.model.member.dto.response.MemberInfoResponse;
import com.grepp.spring.app.controller.api.member.payload.request.MemberUpdateRequest;
import com.grepp.spring.app.controller.api.member.payload.request.PasswordVerifyRequest;
import com.grepp.spring.app.model.member.dto.response.PasswordVerifyResponse;
import com.grepp.spring.app.model.member.service.MemberService;
import com.grepp.spring.infra.response.CommonResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    // 유저 정보 조회(이메일, 닉네임, 아바타)
    @GetMapping("/{memberId}/info")
    public ResponseEntity<CommonResponse<MemberInfoResponse>> getMemberInfo(@PathVariable Long memberId) {

        MemberInfoResponse dto = memberService.getMemberInfo(memberId);

        return ResponseEntity.ok(CommonResponse.success(dto));
    }

    // 개인 정보 수정 (닉네임, 비밀번호 변경)
    @PutMapping("/{memberId}/info")
    public ResponseEntity<CommonResponse<Void>> updateMemberInfo(@PathVariable Long memberId,
        @RequestBody MemberUpdateRequest request) {

        memberService.updateMemberInfo(memberId, request);

        return ResponseEntity.ok(CommonResponse.noContent());
    }

    // 기존 비밀번호 확인
    @PostMapping("/{memberId}/password/verify")
    public ResponseEntity<CommonResponse<PasswordVerifyResponse>> verifyPassword(
        @PathVariable Long memberId,
        @RequestBody PasswordVerifyRequest request) {

        boolean isMatched = memberService.verifyPassword(memberId, request.getPassword());
        var responseData = new PasswordVerifyResponse(isMatched);

        return ResponseEntity.ok(CommonResponse.success(responseData));
    }

    // 유저 정보 요청(닉네임, 우승횟수, 스터디 수, 스터디 종류, 스터디별 출석률, 스터디별 목표달성률, 날짜별 일일 공부시간)
    @GetMapping("/{memberId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> getMember(@PathVariable Long memberId) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", "0000");
        response.put("message", "마이페이지 정보를 성공적으로 불러왔습니다.");
        response.put("data", mockDataMemberId());

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> mockDataMemberId() {

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("nickname", "멋쟁이라이언");
        data.put("winCount", 5);
        data.put("studyCount", 3);
        data.put("rewardPoints", 3000);
        data.put("userStudies", List.of(
            Map.of(
                "studyCategory", "개발",
                "studyName", "자바스크립트 완전정복 스터디",
                "studyStatus", "IN_PROGRESS",
                "maxMembers", 8,
                "currentMembers", 6,
                "attendanceRecords", List.of(
                    Map.of("date", "2024-07-01", "isAttended", true),
                    Map.of("date", "2024-07-02", "isAttended", false),
                    Map.of("date", "2024-07-03", "isAttended", true)
                ),
                "achievementRecords", List.of(
                    Map.of("date", "2024-07-01", "isAchieved", true, "achievedAt", "2024-07-01T10:30:00"),
                    Map.of("date", "2024-07-02", "isAchieved", false, "achievedAt", "2024-07-01T10:30:00"),
                    Map.of("date", "2024-07-03", "isAchieved", true, "achievedAt", "2024-07-03T15:45:10")
                )
            ),
            Map.of(
                "studyCategory", "어학",
                "studyName", "매일 1시간 토익 스터디",
                "studyStatus", "COMPLETED",
                "maxMembers", 5,
                "currentMembers", 5,
                "attendanceRecords", List.of(
                    Map.of("date", "2024-06-25", "isAttended", true)
                ),
                "achievementRecords", List.of(
                    Map.of("date", "2024-06-25", "isAchieved", true, "achievedAt", "2024-06-25T09:00:00")
                )
            )
        ));
        data.put("dailyStudyTimes", List.of(
            Map.of("date", "2024-07-01", "totalStudySeconds", 3600),
            Map.of("date", "2024-07-02", "totalStudySeconds", 1200)
        ));

        return data;
    }

    // 가입 스터디 조회
    @GetMapping("/{memberId}/studies")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> getStudies(@PathVariable Long memberId) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", "0000");
        response.put("message", "가입한 스터디 목록을 조회했습니다.");
        response.put("data", mockDataMemberStudies());

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> mockDataMemberStudies() {

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("memberId", 1);
        data.put("nickname", "홍길동");
        data.put("totalStudyTime", 145800);

        List<Map<String, Object>> studies = List.of(
            Map.of(
                "studyId", 101,
                "title", "토익을 2달만에 만점받자",
                "currentMemberCount", 4,
                "maxMemberCount", 6,
                "category", "어학",
                "start_date", "2025-07-04",
                "end_date", "2025-09-04",
                "repeatSchedule", "매주 화, 수 15:00 ~ 18:00"
            ),
            Map.of(
                "studyId", 102,
                "title", "React를 위해 생존하라!",
                "currentMemberCount", 50,
                "maxMemberCount", 30,
                "category", "프로그래밍",
                "is_survival", true,
                "start_date", "2025-07-04",
                "end_date", "2025-08-04"
            )
        );
        data.put("studies", studies);

        return data;
    }

    // 알람 목록 조회
    @GetMapping("/{memberId}/alarms")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> getMemberAlarms(@PathVariable long memberId) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", "0000");
        response.put("message", "");
        response.put("data", mockDataAlarm());

        return ResponseEntity.ok(response);
    }

    private List<Map<String, Object>> mockDataAlarm() {

        return List.of(
            Map.of(
                "alarmId", 101,
                "type", "ACCEPT",
                "message", "스터디 가입이 승인되었습니다.",
                "isRead", false,
                "sentAt", "2025-07-04T17:35:00"
            ),
            Map.of(
                "alarmId", 100,
                "type", "REJECT",
                "message", "스터디 가입이 거절되었습니다.",
                "isRead", true,
                "sentAt", "2025-07-03T14:12:00"
            )
        );
    }


    // 타이머 누적 시간 조회
    @GetMapping("/{memberId}/timer/all-timer")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> getAllTimer(@PathVariable Long memberId) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", "0000");
        response.put("message", "타이머 누적 시간이 조회되었습니다.");
        response.put("data", mockDataAllTimer());

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> mockDataAllTimer() {

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("userId", 1);
        data.put("nickname", "홍길동");
        data.put("totalStudyTime", 145800);

        return data;
    }

    // 타이머 시간 수정
    @PutMapping("/{memberId}/timer-settings")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> updateTimer(@PathVariable Long memberId) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", "0000");
        response.put("message", "성공적으로 수정하였습니다.");
        response.put("data", new LinkedHashMap<>());

        return ResponseEntity.ok(response);
    }
}