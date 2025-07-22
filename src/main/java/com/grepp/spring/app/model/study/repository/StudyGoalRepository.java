package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.study.entity.StudyGoal;
import com.grepp.spring.app.model.study.reponse.GoalsResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGoalRepository extends JpaRepository<StudyGoal, Long> {

    @Query("select new com.grepp.spring.app.model.study.reponse.GoalsResponse(sg.goalId, sg.content) from StudyGoal sg WHERE sg.study.studyId = :studyId")
    List<GoalsResponse> findGoalsById(Long studyId);

    @Query("SELECT g FROM StudyGoal g WHERE g.study.studyId = :studyId ORDER BY g.goalId ASC")
    List<StudyGoal> findGoalsByStudyId(@Param("studyId") Long studyId);
}
