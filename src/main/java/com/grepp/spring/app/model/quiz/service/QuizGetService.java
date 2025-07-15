package com.grepp.spring.app.model.quiz.service;

import com.grepp.spring.app.controller.api.quiz.payload.QuizListResponse;
import com.grepp.spring.app.model.quiz.dto.QuizDto;
import com.grepp.spring.app.model.quiz.entity.Choice;
import com.grepp.spring.app.model.quiz.entity.Quiz;
import com.grepp.spring.app.model.quiz.entity.QuizSet;
import com.grepp.spring.app.model.quiz.repository.QuizSetRepository;
import com.grepp.spring.infra.error.exceptions.InvalidQuizException;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
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
        List<QuizSet> quizSets = quizSetRepository.findQuizSetsByStudyId(studyId);

        if (quizSets.isEmpty()) {
            throw new NotFoundException("해당 스터디에 대한 퀴즈가 존재하지 않습니다.");
        }

        List<QuizListResponse> result = new ArrayList<>();

        for (QuizSet quizSet : quizSets) {

            List<QuizDto> quizDtos = new ArrayList<>();
            for (Quiz quiz : quizSet.getQuizzes()) {
                QuizDto dto = mapToQuizDto(quiz);
                quizDtos.add(dto);
            }

            result.add(new QuizListResponse(quizSet.getWeek(), quizDtos));
        }

        return result;
    }

    private QuizDto mapToQuizDto(Quiz quiz) {
        Choice choice = quiz.getChoice();
        List<String> choices;

        if (choice != null) {
            choices = List.of(
                    choice.getChoice1(),
                    choice.getChoice2(),
                    choice.getChoice3(),
                    choice.getChoice4()
            );
        } else {
            throw new InvalidQuizException();
        }

        return new QuizDto(
                quiz.getId(),
                quiz.getQuestion(),
                choices
        );
    }
}