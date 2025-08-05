package com.grepp.spring.app.model.studyMmeber.repository.goalAchievement;

import com.grepp.spring.app.model.study.dto.response.CheckGoalResponse;
import com.grepp.spring.app.model.study.dto.response.WeeklyAchievementCount;
import com.grepp.spring.app.model.member.entity.QStudyMember;
import com.grepp.spring.app.model.study.entity.QGoalAchievement;
import com.grepp.spring.app.model.study.entity.QStudyGoal;
import com.grepp.spring.app.model.study.entity.StudyGoal;
import com.grepp.spring.app.model.study.dto.response.GoalsResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoalRepositoryCustomImpl implements GoalRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private static final QStudyGoal studyGoal = QStudyGoal.studyGoal;
    private static final QGoalAchievement goalAchievement = QGoalAchievement.goalAchievement;
    private static final QStudyMember studyMember = QStudyMember.studyMember;



    @Override
    public List<CheckGoalResponse> findAchieveStatuses(Long studyId, Long studyMemberId, LocalDateTime now) {
        LocalDateTime startOfWeek = now.toLocalDate()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .atStartOfDay();

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
                goalAchievement.activated.isTrue(),
                goalAchievement.achievedAt.between(startOfWeek, now)
            )
            .where(
                studyGoal.study.studyId.eq(studyId),
                studyGoal.activated.isTrue()
            )
            .orderBy(studyGoal.goalId.asc())
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
    public List<WeeklyAchievementCount> countWeeklyAchievements(
        Long studyId,
        Long studyMemberId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime) {

        // PostgreSQL
        NumberTemplate<Integer> weekExpression = Expressions.numberTemplate(Integer.class,
            "FLOOR((EXTRACT(EPOCH FROM {0}) - EXTRACT(EPOCH FROM {1})) / 604800) + 1",
            goalAchievement.achievedAt,
            studyGoal.study.startDate
        );

        return queryFactory
            .select(Projections.constructor(WeeklyAchievementCount.class,
                weekExpression.as("week"),
                goalAchievement.achievementId.countDistinct()
            ))
            .from(goalAchievement)
            .join(goalAchievement.studyGoal, studyGoal)
            .where(
                studyGoal.study.studyId.eq(studyId),
                goalAchievement.studyMember.studyMemberId.eq(studyMemberId),
                goalAchievement.achievedAt.between(startDateTime, endDateTime),
                goalAchievement.isAccomplished.isTrue()
            )
            .groupBy(weekExpression)
            .orderBy(weekExpression.asc())
            .fetch();
    }

    @Override
    public boolean findSameLog(Long goalId, Long memberId, LocalDate today) {
        Integer fetchResult = queryFactory
            .selectOne()
            .from(goalAchievement)
            .where(
                goalAchievement.studyGoal.goalId.eq(goalId),
                goalAchievement.studyMember.member.id.eq(memberId),
                goalAchievement.isAccomplished.isTrue(),
                goalAchievement.activated.isTrue(),
                goalAchievement.studyMember.activated.isTrue(),
                goalAchievement.achievedAt.between(
                    today.atStartOfDay(),
                    today.plusDays(1).atStartOfDay()
                )
            )
            .fetchFirst();

        return fetchResult != null;
    }

    public List<StudyGoal> findGoalsByStudyId(Long studyId) {
        return queryFactory
                .selectFrom(studyGoal)
                .where(studyGoal.study.studyId.eq(studyId))
                .orderBy(studyGoal.goalId.asc())
                .fetch();
    }


}
