package com.grepp.spring.app.model.quiz.service;

import com.grepp.spring.app.controller.api.quiz.payload.QuizGradingRequest;
import com.grepp.spring.app.controller.api.quiz.payload.QuizGradingResponse;
import com.grepp.spring.app.model.quiz.entity.Quiz;
import com.grepp.spring.app.model.quiz.repository.QuizRepository;
import com.grepp.spring.infra.error.exceptions.InvalidQuizGradeRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizGradingService {

    private final QuizRepository quizRepository;

    @Transactional(readOnly = true)
    public QuizGradingResponse grade(QuizGradingRequest request) {
        List<Quiz> quizzes = quizRepository.findQuizzesByStudyIdAndWeek(
                request.getStudyId(), request.getWeek()
        );

        if (quizzes.size() != request.getAnswerSheet().size()) {
            throw new InvalidQuizGradeRequestException("답안 수가 퀴즈 수와 일치하지 않습니다.");
        }

        int correctCount = 0;
        for (int i = 0; i < quizzes.size(); i++) {
            int correct = quizzes.get(i).getAnswer();
            int submitted = request.getAnswerSheet().get(i);
            if (correct == submitted) correctCount++;
        }

        return new QuizGradingResponse(request.getWeek(), correctCount);
    }
}