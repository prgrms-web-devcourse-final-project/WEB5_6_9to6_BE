package com.grepp.spring.app.model.quiz.service;

import com.grepp.spring.app.controller.api.quiz.payload.QuizGradingRequest;
import com.grepp.spring.app.controller.api.quiz.payload.QuizGradingResponse;
import com.grepp.spring.app.model.quiz.entity.Quiz;
import com.grepp.spring.app.model.quiz.repository.QuizRepository;
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
        List<Quiz> quizzes = quizRepository.findQuizzesByWeek(request.getWeek());

        if (quizzes.size() != request.getAnswerSheet().size()) {
            throw new IllegalArgumentException("답안 수가 퀴즈 수와 일치하지 않습니다.");
        }

        int correctCount = 0;

        for (int i = 0; i < quizzes.size(); i++) {
            Quiz quiz = quizzes.get(i);
            int submittedAnswerIndex = request.getAnswerSheet().get(i);
            int correctAnswerIndex = quiz.getAnswer();

            boolean isCorrect = submittedAnswerIndex == correctAnswerIndex;
            if (isCorrect) correctCount++;
        }

        return new QuizGradingResponse(request.getWeek(), correctCount);
    }
}