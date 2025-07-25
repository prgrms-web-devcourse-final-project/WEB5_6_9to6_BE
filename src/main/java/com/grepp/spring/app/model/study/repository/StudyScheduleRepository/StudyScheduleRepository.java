package com.grepp.spring.app.model.study.repository.StudyScheduleRepository;

import com.grepp.spring.app.model.study.entity.StudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long>, StudyScheduleRepositoryCustom {
}