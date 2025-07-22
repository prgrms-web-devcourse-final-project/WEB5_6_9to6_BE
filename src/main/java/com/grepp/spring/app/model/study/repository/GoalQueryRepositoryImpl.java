package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.controller.api.study.payload.CheckGoalResponse;
import com.grepp.spring.app.model.member.entity.QStudyMember;
import com.grepp.spring.app.model.study.entity.QGoalAchievement;
import com.grepp.spring.app.model.study.entity.QStudyGoal;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoalQueryRepositoryImpl implements GoalQueryRepository {

    private final JPAQueryFactory queryFactory;

    private static final QStudyGoal studyGoal = QStudyGoal.studyGoal;
    private static final QGoalAchievement goalAchievement = QGoalAchievement.goalAchievement;
    private static final QStudyMember studyMember = QStudyMember.studyMember;



    @Override
    public List<CheckGoalResponse> findAchieveStatusesByStudyId(Long studyId, Long studyMemberId) {
        return queryFactory
            .select(
                Projections.constructor(CheckGoalResponse.class,
                    studyGoal.goalId,
                    studyGoal.content,
                    Expressions.asBoolean(goalAchievement.isAccomplished).coalesce(false)
                    )
            )
            .from(studyGoal)
            .leftJoin(goalAchievement)
                .on(
                    studyGoal.goalId.eq(goalAchievement.studyGoal.goalId)
                        .and(goalAchievement.studyMember.studyMemberId.eq(studyMemberId))
                        .and(goalAchievement.activated.isTrue())
                )
            .where(
                studyGoal.study.studyId.eq(studyId)
                    .and(studyGoal.activated.isTrue())
            )
            .fetch();
    }
}
