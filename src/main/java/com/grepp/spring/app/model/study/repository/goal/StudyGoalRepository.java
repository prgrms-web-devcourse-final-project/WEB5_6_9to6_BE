package com.grepp.spring.app.model.study.repository.goal;

import com.grepp.spring.app.model.study.entity.StudyGoal;
import com.grepp.spring.app.model.studymember.repository.goalAchievement.GoalRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGoalRepository extends JpaRepository<StudyGoal, Long>, GoalRepositoryCustom {

}
