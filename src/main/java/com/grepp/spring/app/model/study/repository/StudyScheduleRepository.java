package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.study.code.DayOfWeek;
import com.grepp.spring.app.model.study.entity.StudySchedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long> {
    @Query("SELECT s FROM StudySchedule s JOIN FETCH s.study study " +
            "WHERE s.dayOfWeek = :dayOfWeek " +
            "AND s.startTime = :startTime " +
            "AND study.studyType = 'SURVIVAL'")
    List<StudySchedule> findWithStudyByDayOfWeekAndStartTime(@Param("dayOfWeek") DayOfWeek dayOfWeek, @Param("startTime") String startTime);
}