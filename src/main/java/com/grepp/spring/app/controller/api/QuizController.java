package com.grepp.spring.app.controller.api;


import com.grepp.spring.infra.util.NotFoundException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/v1/quiz", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuizController {

    @GetMapping("/{studyId}/problems")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> getQuizzes(@PathVariable Long studyId) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", "0000");
        response.put("message", "서바이벌 문제 목록을 조회했습니다.");

        Map<String, Object> data = new HashMap<>();
        data.put("week", 1);

        List<Map<String, Object>> quizzes = new ArrayList<>();

        Map<String, Object> quiz1 = new LinkedHashMap<>();
        quiz1.put("quizId", 1001);
        quiz1.put("question", "이진 탐색의 시간복잡도는?");
        quiz1.put("choices", Arrays.asList("O(n)", "O(log n)", "O(n log n)", "O(1)"));

        Map<String, Object> quiz2 = new LinkedHashMap<>();
        quiz2.put("quizId", 1002);
        quiz2.put("question", "DFS는 어떤 자료구조를 사용하나요?");
        quiz2.put("choices", Arrays.asList("큐", "스택", "힙", "그래프"));

        quizzes.add(quiz1);
        quizzes.add(quiz2);

        data.put("quizzes", quizzes);
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{studyId}/problems")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> RegistQuizzes(
        @PathVariable Long studyId,
        @RequestBody(required = false) Map<String, Object> registQuizRequest
    ) {

        {
            if (registQuizRequest == null || registQuizRequest.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", "BAD_REQUEST");
                errorResponse.put("message", "요청 본문이 비어있습니다.");
                return ResponseEntity.badRequest().body(errorResponse);
            }
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("quizSetId", 301);
        data.put("registeredQuizCount", 2);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", "0000");
        response.put("message", "서바이벌 문제를 등록했습니다.");
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/grading")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> GradingQuizzes(
        @RequestBody(required = false) Map<String, Object> gradingRequest
    ) {
        {
            if (gradingRequest == null || gradingRequest.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", "BAD_REQUEST");
                errorResponse.put("message", "요청 본문이 비어있습니다.");
                return ResponseEntity.badRequest().body(errorResponse);
            }
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("week", 2);
        data.put("correctCount", 4);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", "0000");
        response.put("message", "");
        response.put("data", data);

        return ResponseEntity.ok(response);
    }


    @GetMapping("")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> getSurvival() {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", "0000");
        response.put("message", "서바이벌 목록을 조회했습니다..");

        List<Map<String, Object>> studies = new ArrayList<>();

        Map<String, Object> study1 = new LinkedHashMap<>();
        study1.put("studyId", 1);
        study1.put("title", "사법고시와 맞짱 뜨실분?");
        study1.put("currentMemberCount", 5);
        study1.put("maxMemberCount", 30);
        study1.put("status", "ONGOING");
        study1.put("deadline", "2025-07-14T23:59:00");
        study1.put("category", "고시&공무원");
        study1.put("start_date", "2025-07-04");
        study1.put("end_date", "2025-09-04");

        Map<String, Object> study2 = new HashMap<>();
        study2.put("studyId", 2);
        study2.put("title", "React를 위해 생존하라!");
        study2.put("currentMemberCount", 24);
        study2.put("maxMemberCount", 30);
        study2.put("status", "WAITING");
        study2.put("deadline", "2025-07-20T18:00:00");
        study2.put("category", "프로그래밍");
        study2.put("start_date", "2025-07-15");
        study2.put("end_date", "2025-09-15");

        studies.add(study1);
        studies.add(study2);

        response.put("data", studies);

        return ResponseEntity.ok(response);
    }

    @GetMapping("{studyId}/schedule")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> getSurvivalSchedule(@PathVariable Long studyId) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", "0000");
        response.put("message", "서바이벌 일정을 조회했습니다.");

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("studyId", 1);
        data.put("title", "7월 스터디 생존전");
        data.put("startDate", "2025-07-10");
        data.put("endDate", "2025-07-25");
        data.put("startTime", "09:00");
        data.put("endTime", "18:00");
        data.put("daysOfWeek", "MONDAY");
        data.put("status", "ACTIVE");

        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    @PostMapping("{Id}/schedule")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> getSurvivalResult(
        @PathVariable Long Id,
        @RequestBody(required = false) Map<String, Object> QuizRequest

    ) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", "0000");
        response.put("message", "서바이벌 결과가 성공적으로 등록되었습니다.");


        return ResponseEntity.ok(response);
    }


}



