package com.grepp.studium.study_schedule.repos;

import com.grepp.studium.study.domain.Study;
import com.grepp.studium.study_schedule.domain.StudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudyScheduleRepository extends JpaRepository<StudySchedule, String> {

    StudySchedule findFirstByStudy(Study study);

    boolean existsByIdIgnoreCase(String id);

}
