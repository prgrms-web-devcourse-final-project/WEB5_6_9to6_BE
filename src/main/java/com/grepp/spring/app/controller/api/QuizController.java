package com.grepp.spring.app.controller.api;

import com.grepp.spring.app.controller.api.quiz.payload.QuizGradingRequest;
import com.grepp.spring.app.controller.api.quiz.payload.QuizGradingResponse;
import com.grepp.spring.app.controller.api.quiz.payload.QuizListResponse;
import com.grepp.spring.app.controller.api.quiz.payload.SurvivalResultRequest;
import com.grepp.spring.app.model.quiz.service.QuizGetService;
import com.grepp.spring.app.model.quiz.service.QuizGradingService;
import com.grepp.spring.app.model.quiz.service.SurvivalResultService;
import com.grepp.spring.infra.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/quiz", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuizController {

    private final QuizGetService quizGetService;
    private final QuizGradingService quizGradingService;
    private final SurvivalResultService survivalResultService;

    @GetMapping("/{studyId}/problems")
    public ResponseEntity<CommonResponse<List<QuizListResponse>>> getAllQuizProblems(@PathVariable Long studyId) {

        List<QuizListResponse> data = quizGetService.getQuizzesByStudyId(studyId);
        return ResponseEntity.ok(CommonResponse.success(data));
    }

    @PostMapping("/grading")
    public ResponseEntity<CommonResponse<QuizGradingResponse>> gradeQuiz(
            @RequestBody QuizGradingRequest request
    ) {
        QuizGradingResponse result = quizGradingService.grade(request);
        return ResponseEntity.ok(CommonResponse.success(result));
    }

    @PostMapping("/{id}/weeks/{week}/results")
    public ResponseEntity<CommonResponse<Void>> registerSurvivalResult(
            @PathVariable("id") Long studyId,
            @PathVariable int week,
            @RequestBody SurvivalResultRequest request) {

        survivalResultService.registerSurvivalResult(studyId, week, request);
        return ResponseEntity.ok(CommonResponse.success(null));
    }
}