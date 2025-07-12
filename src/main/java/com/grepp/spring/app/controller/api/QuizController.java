package com.grepp.spring.app.controller.api;

import com.grepp.spring.app.controller.api.quiz.payload.QuizListResponse;
import com.grepp.spring.app.model.quiz.service.QuizService;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/quiz", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/{studyId}/problems")
    public ResponseEntity<CommonResponse<List<QuizListResponse>>> getAllQuizProblems(@PathVariable Long studyId) {

        List<QuizListResponse> data = quizService.getQuizzesByStudyId(studyId);
        return ResponseEntity
                .status(ResponseCode.SUCCESS.status())
                .body(CommonResponse.success(data));
    }
}