package com.grepp.spring.app.model.quiz.service;

import com.grepp.spring.app.controller.api.quiz.payload.QuizListResponse;
import com.grepp.spring.app.model.quiz.dto.QuizDto;
import com.grepp.spring.app.model.quiz.entity.ChoiceEntity;
import com.grepp.spring.app.model.quiz.entity.QuizEntity;
import com.grepp.spring.app.model.quiz.entity.QuizSetEntity;
import com.grepp.spring.app.model.quiz.repository.QuizSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizGetService {

    private final QuizSetRepository quizSetRepository;

    // 스터디에 해당하는 퀴즈 정보 가져옴
    @Transactional(readOnly = true)
    public List<QuizListResponse> getQuizzesByStudyId(Long studyId) {
        List<QuizSetEntity> quizSets = quizSetRepository.findQuizSetsByStudyId(studyId);

        List<QuizListResponse> result = new ArrayList<>();

        for (QuizSetEntity quizSet : quizSets) {

            List<QuizDto> quizDtos = new ArrayList<>();
            for (QuizEntity quiz : quizSet.getQuizzes()) {
                QuizDto dto = mapToQuizDto(quiz);
                quizDtos.add(dto);
            }

            result.add(new QuizListResponse(quizSet.getWeek(), quizDtos));
        }

        return result;
    }

    private QuizDto mapToQuizDto(QuizEntity quiz) {
        ChoiceEntity choice = quiz.getChoice();
        List<String> choices;

        if (choice != null) {
            choices = List.of(
                    choice.getChoice1(),
                    choice.getChoice2(),
                    choice.getChoice3(),
                    choice.getChoice4()
            );
        } else {
            choices = List.of();
        }

        return new QuizDto(
                quiz.getQuizId(),
                quiz.getQuestion(),
                choices
        );
    }
}