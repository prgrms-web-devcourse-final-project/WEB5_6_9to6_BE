package com.grepp.spring.app.model.study.repository.studyScheduleRepository;

import com.grepp.spring.app.model.study.code.DayOfWeek;
import com.grepp.spring.app.model.study.code.StudyType;
import com.grepp.spring.app.model.study.entity.StudySchedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;
import static com.grepp.spring.app.model.study.entity.QStudySchedule.studySchedule;
import static com.grepp.spring.app.model.study.entity.QStudy.study;

@RequiredArgsConstructor
public class StudyScheduleRepositoryCustomImpl implements StudyScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StudySchedule> findSurvivalSchedules(DayOfWeek dayOfWeek, String startTime) {
        return queryFactory
                .selectFrom(studySchedule)
                .join(studySchedule.study, study).fetchJoin()
                .where(
                        studySchedule.dayOfWeek.eq(dayOfWeek),
                        studySchedule.startTime.eq(startTime),
                        study.studyType.eq(StudyType.SURVIVAL)
                )
                .fetch();
    }

    @Override
    public List<StudySchedule> findWithStudyByDayOfWeekAndEndTime(DayOfWeek dayOfWeek, String endTime) {
        return queryFactory
                .selectFrom(studySchedule)
                .join(studySchedule.study, study).fetchJoin()
                .where(
                        studySchedule.dayOfWeek.eq(dayOfWeek),
                        studySchedule.endTime.eq(endTime)
                )
                .fetch();
    }
}