package com.grepp.spring.app.model.quiz.service;

import com.grepp.spring.app.controller.api.quiz.payload.QuizListResponse;
import com.grepp.spring.app.model.quiz.dto.QuizDto;
import com.grepp.spring.app.model.quiz.dto.QuizProjection;
import com.grepp.spring.app.model.quiz.repository.QuizSetRepository;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizGetService {

    private final QuizSetRepository quizSetRepository;

    public List<QuizListResponse> getQuizzesByStudyId(Long studyId) {
        List<QuizProjection> projections = quizSetRepository.findQuizSetsByStudyId(studyId);

        Map<Integer, List<QuizDto>> quizzesByWeek = new LinkedHashMap<>();

        for (QuizProjection projection : projections) {
            int week = projection.getWeek();

            quizzesByWeek.computeIfAbsent(week, k -> new ArrayList<>());

            QuizDto quizDto = new QuizDto(
                    projection.getQuizId(),
                    projection.getQuestion(),
                    projection.getChoices(),
                    projection.getAnswer()
            );

            quizzesByWeek.get(week).add(quizDto);
        }

        List<QuizListResponse> result = new ArrayList<>();
        for (Map.Entry<Integer, List<QuizDto>> entry : quizzesByWeek.entrySet()) {
            result.add(new QuizListResponse(entry.getKey(), entry.getValue()));
        }

        result.sort(Comparator.comparingInt(QuizListResponse::getWeek));

        return result;
    }
}
