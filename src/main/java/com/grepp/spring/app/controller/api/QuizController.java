package com.grepp.spring.app.controller.api;

import com.grepp.spring.app.controller.api.quiz.payload.*;
import com.grepp.spring.app.model.quiz.entity.QuizSet;
import com.grepp.spring.app.model.quiz.service.QuizGetService;
import com.grepp.spring.app.model.quiz.service.QuizGradingService;
import com.grepp.spring.app.model.quiz.service.QuizCreateService;
import com.grepp.spring.app.model.quiz.service.SurvivalResultService;
import com.grepp.spring.infra.error.exceptions.Quiz.MemberNotFoundException;
import com.grepp.spring.infra.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@Tag(name = "퀴즈 API", description = "퀴즈 문제 조회, 생성, 채점 및 결과 처리 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/quiz", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuizController {

    private final QuizGetService quizGetService;
    private final QuizGradingService quizGradingService;
    private final SurvivalResultService survivalResultService;
    private final QuizCreateService quizCreateService;

    @Operation(
            summary = "스터디별 퀴즈 문제 목록 조회",
            description = "스터디 ID(`studyId`)를 이용하여 해당 스터디에 생성된 모든 퀴즈 문제 목록을 조회합니다."
    )
    @GetMapping("/{studyId}/problems")
    public ResponseEntity<CommonResponse<List<QuizListResponse>>> findQuizProblems (@PathVariable Long studyId) throws MemberNotFoundException {

        List<QuizListResponse> data = quizGetService.getQuizzesByStudyId(studyId);
        return ResponseEntity.ok(CommonResponse.success(data));
    }

    @Operation(
            summary = "퀴즈 채점",
            description = """
        요청 body에 `QuizGradingRequest`를 포함해야합니다.
                    {
                    	"memberId": 1,
                    	"studyId": 1,
                    	"week": 2, // 주차
                    	"answerSheet": [1, 2, 3, 2, 3]
                    }
        사용자가 제출한 퀴즈 답안을 채점하고, 그 결과를 반환합니다.
        """
    )
    @PostMapping("/grading")
    public ResponseEntity<CommonResponse<QuizGradingResponse>> gradeQuiz(
            @RequestBody QuizGradingRequest request
    ) {
        QuizGradingResponse result = quizGradingService.grade(request);
        return ResponseEntity.ok(CommonResponse.success(result));
    }

    @Operation(
            summary = "서바이벌 결과 등록",
            description = """
        요청 body에 `SurvivalResultRequest`를 포함해야합니다.
                    {
                        "studyMemberId": 101,
                        "isSurvived": true
                    }
        특정 스터디(`studyId`)의 특정 주차(`week`)에 대한 서바이벌 퀴즈 결과를 등록합니다.
        """
    )
    @PostMapping("/{studyId}/weeks/{week}/results")
    public ResponseEntity<CommonResponse<Void>> registerSurvivalResult(
            @PathVariable("studyId") Long studyId,
            @PathVariable int week,
            @RequestBody SurvivalResultRequest request) {

        survivalResultService.processSurvivalResult(studyId, week, request);
        return ResponseEntity.ok(CommonResponse.noContent());
    }

    @Operation(
            summary = "주차별 퀴즈 문제 생성 실제 서비스에서 사용x 테스트 위해서 만든 API",
            description = """
        요청 body에 `QuizRegisterRequest`를 포함해야합니다.
                    {
                      "week": 1
                    }
        스터디 ID(`studyId`)와 주차(`week`) 정보를 받아 해당 주차의 퀴즈 문제를 생성합니다.
        """
    )
    @PostMapping("/{studyId}/problems")
    public ResponseEntity<CommonResponse<QuizRegisterResponse>> createQuizProblems(
            @PathVariable Long studyId,
            @RequestBody QuizRegisterRequest request) throws IOException {

        QuizSet createdQuizSet = quizCreateService.createProblems(studyId, request.getWeek());
        QuizRegisterResponse response = new QuizRegisterResponse(createdQuizSet.getId(), 5);

        return ResponseEntity.ok(CommonResponse.success(response));
    }
}