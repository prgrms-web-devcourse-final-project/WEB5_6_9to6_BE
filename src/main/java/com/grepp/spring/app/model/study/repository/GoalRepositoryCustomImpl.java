package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.controller.api.study.payload.CheckGoalResponse;
import com.grepp.spring.app.model.member.entity.QStudyMember;
import com.grepp.spring.app.model.study.entity.QGoalAchievement;
import com.grepp.spring.app.model.study.entity.QStudyGoal;
import com.grepp.spring.app.model.study.entity.StudyGoal;
import com.grepp.spring.app.model.study.reponse.GoalsResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoalRepositoryCustomImpl implements GoalRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private static final QStudyGoal studyGoal = QStudyGoal.studyGoal;
    private static final QGoalAchievement goalAchievement = QGoalAchievement.goalAchievement;
    private static final QStudyMember studyMember = QStudyMember.studyMember;



    @Override
    public List<CheckGoalResponse> findAchieveStatuses(Long studyId, Long studyMemberId) {
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
                    studyGoal.goalId.eq(goalAchievement.studyGoal.goalId),
                    goalAchievement.studyMember.studyMemberId.eq(studyMemberId),
                    goalAchievement.activated.isTrue()
                )
            .where(
                studyGoal.study.studyId.eq(studyId),
                studyGoal.activated.isTrue()
            )
            .fetch();
    }

    @Override
    public List<GoalsResponse> findStudyGoals(Long studyId) {
        return queryFactory
            .select(
                Projections.constructor(GoalsResponse.class,
                    studyGoal.goalId,
                    studyGoal.content
                    )
            )
            .from(studyGoal)
            .where(
                studyGoal.study.studyId.eq(studyId),
                studyGoal.activated.isTrue()
            )
            .fetch();
    }

    @Override
    public List<StudyGoal> findGoalsByStudyId(Long studyId) {
        return queryFactory
                .selectFrom(studyGoal)
                .where(studyGoal.study.studyId.eq(studyId))
                .orderBy(studyGoal.goalId.asc())
                .fetch();
    }

//    @Override
//    public int getTotalAchievementsCount(Long studyId, Long studyMemberId, LocalDateTime startDateTime,
//        LocalDateTime endDateTime) {
//        Long count = queryFactory
//            .select(goalAchievement.achievementId.countDistinct())
//            .from(goalAchievement)
//            .join(goalAchievement.studyGoal, studyGoal)
//            .join(studyGoal.study, study)
//            .where(
//                study.studyId.eq(studyId),
//                goalAchievement.studyMember.studyMemberId.eq(studyMemberId),
//                goalAchievement.achievedAt.between(startDateTime, endDateTime),
//                goalAchievement.isAccomplished.isTrue()
//            )
//            .fetchOne();
//
//        return count != null ? count.intValue() : 0;
//    }


}
