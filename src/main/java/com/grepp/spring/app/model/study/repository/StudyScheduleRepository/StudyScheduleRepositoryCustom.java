package com.grepp.spring.app.model.study.repository.StudyScheduleRepository;

import com.grepp.spring.app.model.study.code.DayOfWeek;
import com.grepp.spring.app.model.study.entity.StudySchedule;
import java.util.List;

public interface StudyScheduleRepositoryCustom {
    List<StudySchedule> findSurvivalSchedules(DayOfWeek dayOfWeek, String startTime);
}