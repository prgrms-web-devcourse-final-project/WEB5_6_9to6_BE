package com.grepp.studium.goal_achievement.service;

import com.grepp.studium.goal_achievement.domain.GoalAchievement;
import com.grepp.studium.goal_achievement.model.GoalAchievementDTO;
import com.grepp.studium.goal_achievement.repos.GoalAchievementRepository;
import com.grepp.studium.study_goal.domain.StudyGoal;
import com.grepp.studium.study_goal.repos.StudyGoalRepository;
import com.grepp.studium.study_member.domain.StudyMember;
import com.grepp.studium.study_member.repos.StudyMemberRepository;
import com.grepp.studium.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class GoalAchievementService {

    private final GoalAchievementRepository goalAchievementRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyGoalRepository studyGoalRepository;

    public GoalAchievementService(final GoalAchievementRepository goalAchievementRepository,
            final StudyMemberRepository studyMemberRepository,
            final StudyGoalRepository studyGoalRepository) {
        this.goalAchievementRepository = goalAchievementRepository;
        this.studyMemberRepository = studyMemberRepository;
        this.studyGoalRepository = studyGoalRepository;
    }

    public List<GoalAchievementDTO> findAll() {
        final List<GoalAchievement> goalAchievements = goalAchievementRepository.findAll(Sort.by("achievementId"));
        return goalAchievements.stream()
                .map(goalAchievement -> mapToDTO(goalAchievement, new GoalAchievementDTO()))
                .toList();
    }

    public GoalAchievementDTO get(final Integer achievementId) {
        return goalAchievementRepository.findById(achievementId)
                .map(goalAchievement -> mapToDTO(goalAchievement, new GoalAchievementDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final GoalAchievementDTO goalAchievementDTO) {
        final GoalAchievement goalAchievement = new GoalAchievement();
        mapToEntity(goalAchievementDTO, goalAchievement);
        return goalAchievementRepository.save(goalAchievement).getAchievementId();
    }

    public void update(final Integer achievementId, final GoalAchievementDTO goalAchievementDTO) {
        final GoalAchievement goalAchievement = goalAchievementRepository.findById(achievementId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(goalAchievementDTO, goalAchievement);
        goalAchievementRepository.save(goalAchievement);
    }

    public void delete(final Integer achievementId) {
        goalAchievementRepository.deleteById(achievementId);
    }

    private GoalAchievementDTO mapToDTO(final GoalAchievement goalAchievement,
            final GoalAchievementDTO goalAchievementDTO) {
        goalAchievementDTO.setAchievementId(goalAchievement.getAchievementId());
        goalAchievementDTO.setIsAccomplished(goalAchievement.getIsAccomplished());
        goalAchievementDTO.setActivated(goalAchievement.getActivated());
        goalAchievementDTO.setAchievedAt(goalAchievement.getAchievedAt());
        goalAchievementDTO.setStudyMember(goalAchievement.getStudyMember() == null ? null : goalAchievement.getStudyMember().getStudyMemberId());
        goalAchievementDTO.setGoal(goalAchievement.getGoal() == null ? null : goalAchievement.getGoal().getGoalId());
        return goalAchievementDTO;
    }

    private GoalAchievement mapToEntity(final GoalAchievementDTO goalAchievementDTO,
            final GoalAchievement goalAchievement) {
        goalAchievement.setIsAccomplished(goalAchievementDTO.getIsAccomplished());
        goalAchievement.setActivated(goalAchievementDTO.getActivated());
        goalAchievement.setAchievedAt(goalAchievementDTO.getAchievedAt());
        final StudyMember studyMember = goalAchievementDTO.getStudyMember() == null ? null : studyMemberRepository.findById(goalAchievementDTO.getStudyMember())
                .orElseThrow(() -> new NotFoundException("studyMember not found"));
        goalAchievement.setStudyMember(studyMember);
        final StudyGoal goal = goalAchievementDTO.getGoal() == null ? null : studyGoalRepository.findById(goalAchievementDTO.getGoal())
                .orElseThrow(() -> new NotFoundException("goal not found"));
        goalAchievement.setGoal(goal);
        return goalAchievement;
    }

}
