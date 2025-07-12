package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.study.entity.GoalAchievement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalAchievementRepository extends JpaRepository<GoalAchievement, Long> {

    List<GoalAchievement> findAllByStudyMemberAndIsAccomplishedTrue(StudyMember studyMember);

}
