package com.grepp.spring.app.controller.api.study;

import com.grepp.spring.app.controller.api.study.payload.StudySearchRequest;
import com.grepp.spring.app.model.member.service.MemberService;
import com.grepp.spring.app.model.study.dto.StudyListResponse;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.app.model.study.service.StudyService;
import com.grepp.spring.infra.response.CommonResponse;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/studies", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudyController {

    private final MemberService memberService;
    private final StudyService studyService;

    // 출석체크
    @PostMapping("/{studyId}/attendance")
    public ResponseEntity<?> attend(
        @PathVariable Long studyId,
        Authentication authentication
    ) {
        String email = authentication.getName();
        Long studyMemberId = memberService.findStudyMemberId(email, studyId);

        memberService.markAttendance(studyMemberId);

        return ResponseEntity.ok(CommonResponse.success("출석 체크 완료."));
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
        String code = "SUCCESS";
        StudySchedule schedule1 = StudySchedule.builder()
            .dayOfWeek(DayOfWeek.MON)
            .startTime(LocalTime.of(19, 0)) // 오후 7시
            .endTime(LocalTime.of(21, 0))   // 오후 9시
            .build();

        StudySchedule schedule2 = StudySchedule.builder()
            .dayOfWeek(DayOfWeek.TUE)
            .startTime(LocalTime.of(19, 0)) // 오후 7시
            .endTime(LocalTime.of(21, 0))   // 오후 9시
            .build();

        String studyName = "스터디 1"; // 스터디 이름
        StudyCategory category = StudyCategory.LANGUAGE; // 스터디 카테고리
        StudyType type = StudyType.DEFAULT; // 스터디 타입
        int currentMember = 5; // 현재 인원
        int maxMember = 10; // 최대인원
        Region region = Region.ONLINE; // 지역
        String location = null; // 장소
        StudyStatus studyStatus = StudyStatus.ACTIVATE; // 스터디 활동 상태
        List<StudySchedule> schedules = List.of(schedule1, schedule2); // 스터디 일정
        LocalDate startDate = LocalDate.now(); // 시작날짜
        LocalDate endDate = LocalDate.now().plusMonths(2); // 종료날짜
        String introduction = "안녕하세요. 토스 스터디입니다. 잠은 토스 점수에 해롭습니다."; // 스터디 소개글
        String notify = "휴식은 죽어서 하자"; // 스터디 공지
        String externalLink = "https://www.google.com/"; // 외부 강의 링크
        List<Goal> goals = List.of(
            Goal.builder()
                .goalId(1)
                .content("아무나 붙잡고 영어로 대화 2분하기")
                .checkDay(DayOfWeek.SAT)
                .build(),
            Goal.builder()
                .goalId(2)
                .content("미국대사관 가서 영어로 민원넣기")
                .checkDay(DayOfWeek.SAT)
                .build()
        );

        StudyInfoResponse data = StudyInfoResponse.builder()
            .studyName(studyName)
            .category(category)
            .type(type)
            .currentMember(currentMember)
            .maxMember(maxMember)
            .region(region)
            .location(location)
            .studyStatus(studyStatus)
            .schedules(schedules)
            .startDate(startDate)
            .endDate(endDate)
            .introduction(introduction)
            .notify(notify)
            .externalLink(externalLink)
            .goals(goals)
            .build();

        return ResponseEntity.status(200).body(
            CommonResponse.success(data)
        );
    }

    // 스터디 정보 수정
    // TODO 목데이터 추가
    @PutMapping("/{studyId}")
    public ResponseEntity<?> updateStudyInfo(
        @PathVariable Long studyId,
        @RequestBody Map<String, Object> data
    ) {
        return ResponseEntity.status(200).body(
            CommonResponse.noContent()
        );
    }

    // 스터디 신청 목록 조회
    @GetMapping("/{studyId}/appliacnt-list")
    public ResponseEntity<?> getApplicantList(@PathVariable Long studyId) {
        String code = "SUCCESS";
        long userId1 = 1;
        String userName1 = "김유저1";
        String introduction1 = "날 뽑아라";

        long userId2 = 2;
        String userName2 = "김사용자1";
        String introduction2 = "그래, 잴 뽑아라";

        List<Map<String, ? extends Serializable>> data = List.of(
            Map.of("userId", userId1, "userName", userName1, "introduction", introduction1),
            Map.of("userId", userId2, "userName", userName2, "introduction", introduction2)
        );

        return ResponseEntity.status(200).body(
            CommonResponse.success(data)
        );
    }

    // 유저가 스터디 맴버인지 조회
    // NOTE 이건 진짜 유저 정보가 필요할 것같은데?
    @GetMapping("/{studyId}/members/me/check")
    public ResponseEntity<?> isMember(@PathVariable Long studyId) {
        String code = "SUCCESS";
        Map<String, Boolean> matched = Map.of("matched", true);

        return ResponseEntity.status(200).body(
            CommonResponse.success(matched)
        );
    }

    // 스터디 신청 목록 조회
    @GetMapping("/{studyId}/applications")
    public ResponseEntity<?> getApplications(@PathVariable Long studyId) {
        String code = "SUCCESS";
        long applicationId1 = 1;
        long applicationId2 = 2;
        long memberId1 = 3;
        long memberId2 = 4;
        String nickName1 = "김지원자1";
        String nickName2 = "김지원자2";
        String introduction1 = "날 뽑아라";
        String introduction2 = "아니, 날 뽑아라";
        ApplyState state1 = ApplyState.WAIT;
        ApplyState state2 = ApplyState.ACCEPT;

        List<Map<String, Object>> data = List.of(
            Map.of("applicationId1", applicationId1, "memberId", memberId1, "introduction", introduction1, "state", state1),
            Map.of("applicationId1", applicationId2, "memberId", memberId2, "introduction", introduction2, "state", state2)
        );

        return ResponseEntity.status(200).body(CommonResponse.success(data));
    }

    // 스터디 맴버 조회
    @GetMapping("/{studyId}/members")
    public ResponseEntity<?> getMembers(@PathVariable Long studyId) {
        String code = "SUCCESS";
        long studyMemberId1 = 11;
        long studyMemberId2 = 22;
        long memberId1 = 1;
        long memberId2 = 2;
        String nickName1 = "김이박";
        String nickName2 = "박유저";
        StudyRole role1 = StudyRole.LEADER;
        StudyRole role2 = StudyRole.MEMBER;
        String profileImage1 = "https://marketplace.canva.com/O7Muo/MAGaICO7Muo/1/tl/canva-cute-puppy-with-bone-illustration-MAGaICO7Muo.png";
        String profileImage2 = "https://marketplace.canva.com/MQp8I/MAGdTAMQp8I/1/tl/canva-cute-kawaii-dinosaur-character-illustration-MAGdTAMQp8I.png";

        List<Map<String, Object>> data = List.of(
            Map.of("studyMemberId", studyMemberId1, "memberId", memberId1,"nickName", nickName1, "profileImage", profileImage1, "role", role1),
            Map.of("studyMemberId", studyMemberId2, "memberId", memberId2, "nickName", nickName2,"profileImage", profileImage2, "role", role2)
        );

        return ResponseEntity.status(200).body(CommonResponse.success(data));
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
        String code = "SUCCESS";
        long goalId1 = 1;
        long goalId2 = 2;
        String content1 = "영단어 10000000개 외우기";
        String content2 = "원어민과 통화 학습 10분";
        boolean isAccomplished1 = true;
        boolean isAccomplished2 = false;
        LocalDateTime achievedTime1 = LocalDateTime.now();
        LocalDateTime achievedTime2= LocalDateTime.now();

        List<Map<String, Object>> data = List.of(
            Map.of("goalId", goalId1, "content", content1, "isAccimplied", isAccomplished1, "achievedTime", achievedTime1),
            Map.of("goalId", goalId2, "content", content2, "isAccimplied", isAccomplished2, "achievedTime", achievedTime2)
        );

        return ResponseEntity.status(200).body(CommonResponse.success(data));
    }

    // 스터디 목표 달성
    @PostMapping("/{studyId}/goal/{goalId}")
    public ResponseEntity<?> successGoal(@PathVariable Long studyId, @PathVariable Long goalId) {
        return ResponseEntity.status(200).body(
            CommonResponse.noContent()
        );
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

    @Getter
    public static class StudyInfoResponse {
        private String studyName;
        private StudyCategory category;
        private StudyType type;
        private int currentMember;
        private int maxMember;
        private Region region;
        private String location;
        private StudyStatus studyStatus;
        private List<StudySchedule> schedules;
        private LocalDate startDate;
        private LocalDate endDate;
        private String introduction;
        private String notify;
        private String externalLink;
        private List<Goal> goals;

        @Builder
        public StudyInfoResponse(String studyName, StudyCategory category, StudyType type,
            int currentMember, int maxMember, Region region, String location,
            StudyStatus studyStatus,
            List<StudySchedule> schedules, LocalDate startDate, LocalDate endDate,
            String introduction,
            String notify, String externalLink, List<Goal> goals) {
            this.studyName = studyName;
            this.category = category;
            this.type = type;
            this.currentMember = currentMember;
            this.maxMember = maxMember;
            this.region = region;
            this.location = location;
            this.studyStatus = studyStatus;
            this.schedules = schedules;
            this.startDate = startDate;
            this.endDate = endDate;
            this.introduction = introduction;
            this.notify = notify;
            this.externalLink = externalLink;
            this.goals = goals;
        }
    }



//    @Data
//    public static class StudyListResponse{
//        private StudyType studyType;
//        private String name;
//        private StudyCategory studyCategory;
//        private Region region;
//        private LocalDate startDate;
//        private int currentMember;
//        private int maxMember;
//        private List<StudySchedule> studySchedule;
//
//        @Builder
//        public StudyListResponse(StudyType studyType, String name, StudyCategory studyCategory,
//            Region region, LocalDate startDate, int currentMember, int maxMember,
//            List<StudySchedule> studySchedule) {
//            this.studyType = studyType;
//            this.name = name;
//            this.studyCategory = studyCategory;
//            this.region = region;
//            this.startDate = startDate;
//            this.currentMember = currentMember;
//            this.maxMember = maxMember;
//            this.studySchedule = studySchedule;
//        }
//    }


    private enum StudyRole {
        MEMBER, LEADER;
    }

    private enum ApplyState {
        WAIT, ACCEPT, REJECT;
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

    // 스터디 활동 상태
    private enum StudyStatus {
        READY, ACTIVATE
    }

    // 진행 요일
    private enum DayOfWeek {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }

}
