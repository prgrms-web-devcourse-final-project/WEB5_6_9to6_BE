package com.grepp.studium.study_goal.service;

import com.grepp.studium.goal_achievement.domain.GoalAchievement;
import com.grepp.studium.goal_achievement.repos.GoalAchievementRepository;
import com.grepp.studium.study.domain.Study;
import com.grepp.studium.study.repos.StudyRepository;
import com.grepp.studium.study_goal.domain.StudyGoal;
import com.grepp.studium.study_goal.model.StudyGoalDTO;
import com.grepp.studium.study_goal.repos.StudyGoalRepository;
import com.grepp.studium.util.NotFoundException;
import com.grepp.studium.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class StudyGoalService {

    private final StudyGoalRepository studyGoalRepository;
    private final StudyRepository studyRepository;
    private final GoalAchievementRepository goalAchievementRepository;

    public StudyGoalService(final StudyGoalRepository studyGoalRepository,
            final StudyRepository studyRepository,
            final GoalAchievementRepository goalAchievementRepository) {
        this.studyGoalRepository = studyGoalRepository;
        this.studyRepository = studyRepository;
        this.goalAchievementRepository = goalAchievementRepository;
    }

    public List<StudyGoalDTO> findAll() {
        final List<StudyGoal> studyGoals = studyGoalRepository.findAll(Sort.by("goalId"));
        return studyGoals.stream()
                .map(studyGoal -> mapToDTO(studyGoal, new StudyGoalDTO()))
                .toList();
    }

    public StudyGoalDTO get(final Integer goalId) {
        return studyGoalRepository.findById(goalId)
                .map(studyGoal -> mapToDTO(studyGoal, new StudyGoalDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final StudyGoalDTO studyGoalDTO) {
        final StudyGoal studyGoal = new StudyGoal();
        mapToEntity(studyGoalDTO, studyGoal);
        return studyGoalRepository.save(studyGoal).getGoalId();
    }

    public void update(final Integer goalId, final StudyGoalDTO studyGoalDTO) {
        final StudyGoal studyGoal = studyGoalRepository.findById(goalId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(studyGoalDTO, studyGoal);
        studyGoalRepository.save(studyGoal);
    }

    public void delete(final Integer goalId) {
        studyGoalRepository.deleteById(goalId);
    }

    private StudyGoalDTO mapToDTO(final StudyGoal studyGoal, final StudyGoalDTO studyGoalDTO) {
        studyGoalDTO.setGoalId(studyGoal.getGoalId());
        studyGoalDTO.setContent(studyGoal.getContent());
        studyGoalDTO.setType(studyGoal.getType());
        studyGoalDTO.setActivated(studyGoal.getActivated());
        studyGoalDTO.setStudy(studyGoal.getStudy() == null ? null : studyGoal.getStudy().getStudyId());
        return studyGoalDTO;
    }

    private StudyGoal mapToEntity(final StudyGoalDTO studyGoalDTO, final StudyGoal studyGoal) {
        studyGoal.setContent(studyGoalDTO.getContent());
        studyGoal.setType(studyGoalDTO.getType());
        studyGoal.setActivated(studyGoalDTO.getActivated());
        final Study study = studyGoalDTO.getStudy() == null ? null : studyRepository.findById(studyGoalDTO.getStudy())
                .orElseThrow(() -> new NotFoundException("study not found"));
        studyGoal.setStudy(study);
        return studyGoal;
    }

    public ReferencedWarning getReferencedWarning(final Integer goalId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final StudyGoal studyGoal = studyGoalRepository.findById(goalId)
                .orElseThrow(NotFoundException::new);
        final GoalAchievement goalGoalAchievement = goalAchievementRepository.findFirstByGoal(studyGoal);
        if (goalGoalAchievement != null) {
            referencedWarning.setKey("studyGoal.goalAchievement.goal.referenced");
            referencedWarning.addParam(goalGoalAchievement.getAchievementId());
            return referencedWarning;
        }
        return null;
    }

}
