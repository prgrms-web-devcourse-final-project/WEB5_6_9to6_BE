package com.grepp.spring.app.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/members")
public class MemberController {

    @GetMapping("/{memberId}")
    public ResponseEntity<Map<String, Object>> getMember(@PathVariable Integer memberId) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", "SUCCESS");
        response.put("message", "마이페이지 정보를 성공적으로 불러왔습니다.");
        response.put("data", MockDataMemberId());

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> MockDataMemberId() {
        Map<String, Object> data = new HashMap<>();
        data.put("nickname", "멋쟁이라이언");
        data.put("winCount", 5);
        data.put("studyCount", 3);
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
                                Map.of("date", "2024-07-02", "isAchieved", false, "achievedAt", null),
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

    @GetMapping("/{memberId}/timer/all-timer")
    public ResponseEntity<Map<String, Object>> createMember(@PathVariable Integer memberId) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", "SUCCESS");
        response.put("message", "타이머 누적 시간이 조회되었습니다.");
        response.put("data", MockDataAllTimer());
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> MockDataAllTimer() {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", 1);
        data.put("nickname", "홍길동");
        data.put("totalStudyTime", 145800);
        return data;
    }

    @PutMapping("/{memberId}/studies")
    public ResponseEntity<Map<String, Object>> updateMember(@PathVariable Integer memberId) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", "SUCCESS");
        response.put("message", "가입한 스터디 목록을 조회했습니다.");
        response.put("data", MockDataMemberIdStudies());

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> MockDataMemberIdStudies() {
        Map<String, Object> data = new HashMap<>();
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

    @PutMapping("/{memberId}/timer-settings")
    public ResponseEntity<Map<String, Object>> updateTimerSettings(@PathVariable Integer memberId) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", "SUCCESS");
        response.put("message", "성공적으로 수정하였습니다.");
        response.put("data", new HashMap<>());
        return ResponseEntity.ok(response);
    }

}
