package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.controller.api.study.payload.CheckGoalResponse;
import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.study.entity.GoalAchievement;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalAchievementRepository extends JpaRepository<GoalAchievement, Long>, GoalQueryRepository {

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

//    @Query("select new com.grepp.spring.app.controller.api.study.payload.CheckGoalResponse("
//        + "sg.goalId, sg.content, COALESCE(ga.isAccomplished, FALSE)) "
//        + "from StudyGoal sg "
//        + "left join GoalAchievement ga on sg.goalId = ga.studyGoal.goalId "
//        + "and ga.studyMember.studyMemberId = :studyMemberId "
//        + "and ga.activated = TRUE "
//        + "where sg.study.studyId = :studyId "
//        + "and sg.activated = TRUE")
//    List<CheckGoalResponse> findAchieveStatusesByStudyId(@Param("studyId") Long studyId, @Param("studyMemberId") Long studyMemberId);
}
