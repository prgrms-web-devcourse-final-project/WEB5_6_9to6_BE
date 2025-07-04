package com.grepp.studium.goal_achievement.repos;

import com.grepp.studium.goal_achievement.domain.GoalAchievement;
import com.grepp.studium.study_goal.domain.StudyGoal;
import com.grepp.studium.study_member.domain.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GoalAchievementRepository extends JpaRepository<GoalAchievement, Integer> {

    GoalAchievement findFirstByStudyMember(StudyMember studyMember);

    GoalAchievement findFirstByGoal(StudyGoal studyGoal);

}
