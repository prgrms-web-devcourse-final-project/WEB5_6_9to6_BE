package com.grepp.spring.app.model.quiz.repository.quizRepository;

import com.grepp.spring.app.model.quiz.entity.Quiz;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.grepp.spring.app.model.quiz.entity.QQuiz.quiz;
import static com.grepp.spring.app.model.quiz.entity.QQuizSet.quizSet;

@RequiredArgsConstructor
public class QuizRepositoryCustomImpl implements QuizRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Quiz> findQuizzesByStudyIdAndWeek(Long studyId, int week) {
        return queryFactory
                .selectFrom(quiz)
                .join(quiz.quizSet, quizSet)
                .where(
                        quizSet.studyId.eq(studyId),
                        quizSet.week.eq(week),
                        quiz.activated.isTrue()
                )
                .orderBy(quiz.id.asc())
                .fetch();
    }
}