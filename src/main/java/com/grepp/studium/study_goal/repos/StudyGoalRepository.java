package com.grepp.studium.study_goal.repos;

import com.grepp.studium.study.domain.Study;
import com.grepp.studium.study_goal.domain.StudyGoal;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudyGoalRepository extends JpaRepository<StudyGoal, Integer> {

    StudyGoal findFirstByStudy(Study study);

}
