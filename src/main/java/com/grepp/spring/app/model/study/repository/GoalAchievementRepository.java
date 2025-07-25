package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.study.entity.GoalAchievement;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalAchievementRepository extends JpaRepository<GoalAchievement, Long>,
    GoalRepositoryCustom {

    @Query("""
SELECT COUNT(DISTINCT ga.achievementId)
FROM GoalAchievement ga
JOIN ga.studyGoal sg
WHERE sg.study.studyId = :studyId
  AND ga.studyMember.studyMemberId = :studyMemberId
  AND ga.achievedAt BETWEEN :startDateTime AND :endDateTime
  AND ga.isAccomplished = true
""")
    int countTotalAchievements(
        @Param("studyId") Long studyId,
        @Param("studyMemberId") Long studyMemberId,
        @Param("startDateTime") LocalDateTime startDateTime,
        @Param("endDateTime") LocalDateTime endDateTime
    );

    List<GoalAchievement> findAllByStudyMemberAndIsAccomplishedTrue(StudyMember studyMember);

}
