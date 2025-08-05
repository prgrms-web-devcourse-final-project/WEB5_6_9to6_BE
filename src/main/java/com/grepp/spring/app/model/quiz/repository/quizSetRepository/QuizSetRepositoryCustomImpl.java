package com.grepp.spring.app.model.quiz.repository.quizSetRepository;

import com.grepp.spring.app.model.quiz.dto.maybeResponse.QuizProjection;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;
import static com.grepp.spring.app.model.quiz.entity.QChoice.choice;
import static com.grepp.spring.app.model.quiz.entity.QQuiz.quiz;
import static com.grepp.spring.app.model.quiz.entity.QQuizSet.quizSet;

@RequiredArgsConstructor
public class QuizSetRepositoryCustomImpl implements QuizSetRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<QuizProjection> findQuizSetsByStudyId(Long studyId) {
        return queryFactory
                .select(Projections.constructor(QuizProjection.class,
                        quizSet.week,
                        quiz.id,
                        quiz.question,
                        choice.choice1,
                        choice.choice2,
                        choice.choice3,
                        choice.choice4,
                        quiz.answer
                ))
                .from(quizSet)
                .join(quizSet.quizzes, quiz)
                .join(quiz.choice, choice)
                .where(
                        quizSet.studyId.eq(studyId),
                        quizSet.activated.isTrue(),
                        quiz.activated.isTrue()
                )
                .orderBy(quizSet.week.asc(), quiz.id.asc())
                .fetch();
    }
}