package com.grepp.studium.goal_achievement.rest;

import com.grepp.studium.goal_achievement.model.GoalAchievementDTO;
import com.grepp.studium.goal_achievement.service.GoalAchievementService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/goalAchievements", produces = MediaType.APPLICATION_JSON_VALUE)
public class GoalAchievementResource {

    private final GoalAchievementService goalAchievementService;

    public GoalAchievementResource(final GoalAchievementService goalAchievementService) {
        this.goalAchievementService = goalAchievementService;
    }

    @GetMapping
    public ResponseEntity<List<GoalAchievementDTO>> getAllGoalAchievements() {
        return ResponseEntity.ok(goalAchievementService.findAll());
    }

    @GetMapping("/{achievementId}")
    public ResponseEntity<GoalAchievementDTO> getGoalAchievement(
            @PathVariable(name = "achievementId") final Integer achievementId) {
        return ResponseEntity.ok(goalAchievementService.get(achievementId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createGoalAchievement(
            @RequestBody @Valid final GoalAchievementDTO goalAchievementDTO) {
        final Integer createdAchievementId = goalAchievementService.create(goalAchievementDTO);
        return new ResponseEntity<>(createdAchievementId, HttpStatus.CREATED);
    }

    @PutMapping("/{achievementId}")
    public ResponseEntity<Integer> updateGoalAchievement(
            @PathVariable(name = "achievementId") final Integer achievementId,
            @RequestBody @Valid final GoalAchievementDTO goalAchievementDTO) {
        goalAchievementService.update(achievementId, goalAchievementDTO);
        return ResponseEntity.ok(achievementId);
    }

    @DeleteMapping("/{achievementId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteGoalAchievement(
            @PathVariable(name = "achievementId") final Integer achievementId) {
        goalAchievementService.delete(achievementId);
        return ResponseEntity.noContent().build();
    }

}
