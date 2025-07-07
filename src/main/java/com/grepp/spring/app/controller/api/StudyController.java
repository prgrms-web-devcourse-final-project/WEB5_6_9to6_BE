package com.grepp.spring.app.controller.api;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/studies", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudyController {

    // 출석체크
    @PostMapping("/{studyId}/attendance")
    public ResponseEntity<?> attend(@PathVariable Long studyId) {
        String code = "SUCCESS";
        String message = "출석 완료";

        return ResponseEntity.status(200).body(Map.of("code", code, "message", message));
    }

    // 스터디 목록(검색)
    @PostMapping()
    public ResponseEntity<?> searchStudies(
        @RequestBody StudySearchRequest req
    ) {
        String code = "SUCCESS";
        StudySchedule std1Schedule = StudySchedule.builder()
            .dayOfWeek(DayOfWeek.MON)
            .startTime(LocalTime.of(12, 0))
            .endTime(LocalTime.of(13, 0))
            .build();
        StudyListResponse std1 = StudyListResponse.builder()
            .studyType(StudyType.DEFAULT)
            .name("토익 100점 스터디")
            .studyCategory(StudyCategory.LANGUAGE)
            .region(Region.ONLINE)
            .startDate(LocalDate.now())
            .currentMember(4)
            .maxMember(10)
            .studySchedule(List.of(std1Schedule))
            .build(); // 일반, 어학, 온라인, 7월

        List<StudySchedule> std2Schedule = List.of(
            StudySchedule.builder()
                .dayOfWeek(DayOfWeek.MON)
                .startTime(LocalTime.of(18, 0))
                .endTime(LocalTime.of(20, 0))
                .build(),
            StudySchedule.builder()
                .dayOfWeek(DayOfWeek.FRI)
                .startTime(LocalTime.of(18, 0))
                .endTime(LocalTime.of(20, 0))
                .build()
        );
        StudyListResponse std2 = StudyListResponse.builder()
            .studyType(StudyType.DEFAULT)
            .name("토스 만점 스터디")
            .studyCategory(StudyCategory.LANGUAGE)
            .region(Region.SEOUL)
            .startDate(LocalDate.now().minusMonths(1))
            .currentMember(5)
            .maxMember(6)
            .studySchedule(std2Schedule)
            .build(); // 일반, 어학, 서울, 8월

        List<StudySchedule> std3Schedule = List.of(
            StudySchedule.builder()
                .dayOfWeek(DayOfWeek.THU)
                .startTime(LocalTime.of(18, 0))
                .endTime(LocalTime.of(20, 0))
                .build(),
            StudySchedule.builder()
                .dayOfWeek(DayOfWeek.SAT)
                .startTime(LocalTime.of(18, 0))
                .endTime(LocalTime.of(20, 0))
                .build()
        );
        StudyListResponse std3 = StudyListResponse.builder()
            .studyType(StudyType.DEFAULT)
            .name("node.js 마스터 스터디")
            .studyCategory(StudyCategory.PROGRAMMING)
            .region(Region.SEOUL)
            .startDate(LocalDate.now().minusMonths(2))
            .currentMember(7)
            .maxMember(7)
            .studySchedule(std3Schedule)
            .build(); // 일반, 프로그래밍, 서울, 9월

        List<StudySchedule> std4Schedule = List.of(
            StudySchedule.builder()
                .dayOfWeek(DayOfWeek.MON)
                .startTime(LocalTime.of(18, 0))
                .endTime(LocalTime.of(20, 0))
                .build(),
            StudySchedule.builder()
                .dayOfWeek(DayOfWeek.WED)
                .startTime(LocalTime.of(18, 0))
                .endTime(LocalTime.of(20, 0))
                .build()
        );
        StudyListResponse std4 = StudyListResponse.builder()
            .studyType(StudyType.DEFAULT)
            .name("코틀린 안드로이드 개발 스터디")
            .studyCategory(StudyCategory.PROGRAMMING)
            .region(Region.JEJU)
            .startDate(LocalDate.now().minusMonths(3))
            .currentMember(3)
            .maxMember(10)
            .build(); // 일반, 프로그래밍, 제주, 10월

        StudySchedule std5Schedule = StudySchedule.builder()
            .dayOfWeek(DayOfWeek.TUE)
            .startTime(LocalTime.of(12, 0))
            .endTime(LocalTime.of(13, 0))
            .build();
        StudyListResponse std5 = StudyListResponse.builder()
            .studyType(StudyType.SURVIVAL)
            .name("토익 서바이벌")
            .studyCategory(StudyCategory.LANGUAGE)
            .region(Region.ONLINE)
            .startDate(LocalDate.now().minusMonths(2))
            .currentMember(14)
            .maxMember(30)
            .studySchedule(List.of(std5Schedule))
            .build(); // 서바이벌, 어학, 9월 시작

        StudySchedule std6Schedule = StudySchedule.builder()
            .dayOfWeek(DayOfWeek.SUN)
            .startTime(LocalTime.of(12, 0))
            .endTime(LocalTime.of(13, 0))
            .build();
        StudyListResponse std6 = StudyListResponse.builder()
            .studyType(StudyType.SURVIVAL)
            .name("JLPT N2 서바이벌")
            .studyCategory(StudyCategory.LANGUAGE)
            .region(Region.ONLINE)
            .startDate(LocalDate.now().minusMonths(1))
            .currentMember(27)
            .maxMember(30)
            .studySchedule(List.of(std6Schedule))
            .build(); // 서바이벌, 어학, 8월 시작

        // 검색
        List<StudyListResponse> filteredStudies = new java.util.ArrayList<>();
        List<StudyListResponse> allStudies = List.of(std1, std2, std3, std4, std5, std6);
        for (StudyListResponse study : allStudies) {
            if ((req.getCategory() == null || req.getCategory() == study.studyCategory) &&
                (req.getRegion() == null || req.getRegion() == study.region) &&
                (req.getStatus() == null || req.getStatus() == StudyStatus.ACTIVATE) &&
                (req.getTitle() == null || study.name.contains(req.getTitle()))) {
                filteredStudies.add(study);
            }
        }
        return ResponseEntity.status(200).body(Map.of("code", code, "data", filteredStudies));
    }

    @Data
    public static class StudyListResponse{
        private StudyType studyType;
        private String name;
        private StudyCategory studyCategory;
        private Region region;
        private LocalDate startDate;
        private int currentMember;
        private int maxMember;
        private List<StudySchedule> studySchedule;

        @Builder
        public StudyListResponse(StudyType studyType, String name, StudyCategory studyCategory,
            Region region, LocalDate startDate, int currentMember, int maxMember,
            List<StudySchedule> studySchedule) {
            this.studyType = studyType;
            this.name = name;
            this.studyCategory = studyCategory;
            this.region = region;
            this.startDate = startDate;
            this.currentMember = currentMember;
            this.maxMember = maxMember;
            this.studySchedule = studySchedule;
        }
    }

    // 스터디 정보 조회
    // TODO 스터디 목표 필드 추가
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
        // TODO 스터디 목표 추가

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
            .build();

        return ResponseEntity.status(200).body(
            Map.of("code", code,"data", data)
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
            Map.of(
                "code", "SUCCESS",
                "message", "스터디 정보가 수정되었습니다."
            )
        );
    }

    // 스터디 신청 목록 조회
    @GetMapping("/{studyId}/appliacnt-list")
    public ResponseEntity<?> getApplicantList(@PathVariable Long studyId) {
        String code = "SUCCESS";
        int userId1 = 1;
        String userName1 = "김유저1";
        String introduction1 = "날 뽑아라";

        int userId2 = 2;
        String userName2 = "김사용자1";
        String introduction2 = "그래, 잴 뽑아라";

        List<Map<String, ? extends Serializable>> data = List.of(
            Map.of("userId", userId1, "userName", userName1, "introduction", introduction1),
            Map.of("userId", userId2, "userName", userName2, "introduction", introduction2)
        );

        return ResponseEntity.status(200).body(
            Map.of("code", code, "data", data)
        );
    }

    // 유저가 스터디 맴버인지 조회
    // NOTE 이건 진짜 유저 정보가 필요할 것같은데?
    @GetMapping("/{studyId}/members/me/check")
    public ResponseEntity<?> isMember(@PathVariable Long studyId) {
        String code = "SUCCESS";
        Map<String, Boolean> matched = Map.of("matched", true);

        return ResponseEntity.status(200).body(
            Map.of(
                "code", "SUCCESS",
                "data", matched
            )
        );
    }

    // 스터디 맴버 조회


    // 스터디 생성
    // 스터디 목표 조회
    // 스터디 목표 달성

    @Data
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

        @Builder
        public StudyInfoResponse(String studyName, StudyCategory category, StudyType type,
            int currentMember, int maxMember, Region region, String location,
            StudyStatus studyStatus,
            List<StudySchedule> schedules, LocalDate startDate, LocalDate endDate,
            String introduction,
            String notify, String externalLink) {
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
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class StudySearchRequest { // 스터디 검색 요청
        private StudyCategory category;
        private Region region;
        private StudyStatus status;
        private String title;
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
