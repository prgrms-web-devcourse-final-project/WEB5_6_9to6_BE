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
public class QuizGetService {

    private final QuizSetRepository quizSetRepository;

    @Transactional(readOnly = true)
    public List<QuizListResponse> getQuizzesByStudyId(Long studyId) {
        List<QuizProjection> projections = quizSetRepository.findQuizSetsByStudyId(studyId);

        if (projections.isEmpty()) {
            throw new NotFoundException("해당 스터디에 대한 퀴즈가 존재하지 않습니다.");
        }

        Map<Integer, List<QuizDto>> quizMap = new LinkedHashMap<>();

        for (QuizProjection p : projections) {
            QuizDto dto = new QuizDto(
                    p.getQuizId(),
                    p.getQuestion(),
                    List.of(p.getChoice1(), p.getChoice2(), p.getChoice3(), p.getChoice4())
            );
            Integer week = p.getWeek();
            if (!quizMap.containsKey(week)) {
                quizMap.put(week, new ArrayList<>());
            }
            quizMap.get(week).add(dto);
        }

        List<QuizListResponse> responses = new ArrayList<>();
        for (Integer week : quizMap.keySet()) {
            List<QuizDto> quizDtos = quizMap.get(week);
            QuizListResponse response = new QuizListResponse(week, quizDtos);
            responses.add(response);
        }

        return responses;
    }
}
