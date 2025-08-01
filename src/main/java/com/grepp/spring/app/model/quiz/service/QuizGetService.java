package com.grepp.spring.app.model.quiz.service;

import com.grepp.spring.app.controller.api.quiz.payload.QuizListResponse;
import com.grepp.spring.app.model.quiz.dto.QuizDto;
import com.grepp.spring.app.model.quiz.dto.QuizProjection;
import com.grepp.spring.app.model.quiz.repository.quizSetRepository.QuizSetRepository;
import com.grepp.spring.app.model.study.repository.StudyRepository;
import com.grepp.spring.infra.error.exceptions.Quiz.InvalidQuizException;
import com.grepp.spring.infra.error.exceptions.Quiz.StudyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizGetService {

    private final QuizSetRepository quizSetRepository;
    private final StudyRepository studyRepository;

    // 스터디에 속한 모든 퀴즈 조회
    public List<QuizListResponse> getQuizzes(Long studyId) {

        if (!studyRepository.existsById(studyId)) {
            throw new StudyNotFoundException("존재하지 않는 스터디입니다. studyId: " + studyId);
        }

        List<QuizProjection> projections = quizSetRepository.findQuizSetsByStudyId(studyId);

        if (projections.isEmpty()) {
            throw new InvalidQuizException("해당 스터디에 생성된 퀴즈가 없습니다. studyId: " + studyId);
        }

        Map<Integer, List<QuizDto>> quizzesByWeek = new LinkedHashMap<>();

        for (QuizProjection projection : projections) {
            int week = projection.getWeek();

            if (!quizzesByWeek.containsKey(week)) {
                quizzesByWeek.put(week, new ArrayList<>());
            }

            QuizDto quizDto = new QuizDto(
                    projection.getQuizId(),
                    projection.getQuestion(),
                    projection.getChoices(),
                    projection.getAnswer()
            );

            quizzesByWeek.get(week).add(quizDto);
        }

        List<QuizListResponse> responseList = new ArrayList<>();

        for (Map.Entry<Integer, List<QuizDto>> entry : quizzesByWeek.entrySet()) {
            int week = entry.getKey();
            List<QuizDto> quizList = entry.getValue();

            QuizListResponse response = new QuizListResponse(week, quizList);
            responseList.add(response);
        }

        return responseList;
    }
}
