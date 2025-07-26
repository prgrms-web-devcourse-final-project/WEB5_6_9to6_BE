package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.study.entity.StudyGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGoalRepository extends JpaRepository<StudyGoal, Long>, GoalRepositoryCustom {

}
