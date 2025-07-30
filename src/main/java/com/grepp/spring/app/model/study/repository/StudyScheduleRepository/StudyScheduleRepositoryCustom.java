package com.grepp.spring.app.model.study.repository.StudyScheduleRepository;

import com.grepp.spring.app.model.study.code.DayOfWeek;
import com.grepp.spring.app.model.study.entity.StudySchedule;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;

public interface StudyScheduleRepositoryCustom {
    @EntityGraph(attributePaths = {"study"})
    List<StudySchedule> findSurvivalSchedules(DayOfWeek dayOfWeek, String startTime);

    @EntityGraph(attributePaths = {"study"})
    List<StudySchedule> findWithStudyByDayOfWeekAndEndTime(DayOfWeek dayOfWeek, String endTime);
}