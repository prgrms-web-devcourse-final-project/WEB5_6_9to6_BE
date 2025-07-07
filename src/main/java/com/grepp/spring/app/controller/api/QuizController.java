package com.grepp.spring.app.controller.api;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, Object>> getQuizzes(@PathVariable Long studyId) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", "SUCCESS");
        response.put("message", "서바이벌 문제 목록을 조회했습니다.");

        Map<String, Object> data = new HashMap<>();
        data.put("week", 1);

        List<Map<String, Object>> quizzes = new ArrayList<>();

        Map<String, Object> quiz1 = new HashMap<>();
        quiz1.put("quizId", 1001);
        quiz1.put("question", "이진 탐색의 시간복잡도는?");
        quiz1.put("choices", Arrays.asList("O(n)", "O(log n)", "O(n log n)", "O(1)"));

        Map<String, Object> quiz2 = new HashMap<>();
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
    public ResponseEntity<Map<String,Object>> RegistQuizzes(
        @PathVariable Long studyId,
        @RequestBody(required = false) Map<String, Object> RegistQuizRequest
        ) {
        Map<String, Object> data = new HashMap<>();
        data.put("quizSetId", 301);
        data.put("registeredQuizCount", 2);

        Map<String, Object> response = new HashMap<>();
        response.put("code", "SUCCESS");
        response.put("message", "서바이벌 문제를 등록했습니다.");
        response.put("data", data);


        return ResponseEntity.ok(response);
    }

    @PostMapping("/grading")
    public ResponseEntity<Map<String,Object>> GradingQuizzes(
        @RequestBody(required = false) Map<String, Object> GradingRequest
    ) {
        Map<String, Object> data = new HashMap<>();
        data.put("week", 2);
        data.put("correctCount", 4);

        Map<String, Object> response = new HashMap<>();
        response.put("code", "SUCCESS");
        response.put("message", "");
        response.put("data", data);


        return ResponseEntity.ok(response);
    }


}



