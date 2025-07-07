package com.grepp.studium.study_goal.rest;

import com.grepp.studium.study_goal.model.StudyGoalDTO;
import com.grepp.studium.study_goal.service.StudyGoalService;
import com.grepp.studium.util.ReferencedException;
import com.grepp.studium.util.ReferencedWarning;
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
@RequestMapping(value = "/api/studyGoals", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudyGoalResource {

    private final StudyGoalService studyGoalService;

    public StudyGoalResource(final StudyGoalService studyGoalService) {
        this.studyGoalService = studyGoalService;
    }

    @GetMapping
    public ResponseEntity<List<StudyGoalDTO>> getAllStudyGoals() {
        return ResponseEntity.ok(studyGoalService.findAll());
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<StudyGoalDTO> getStudyGoal(
            @PathVariable(name = "goalId") final Integer goalId) {
        return ResponseEntity.ok(studyGoalService.get(goalId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createStudyGoal(
            @RequestBody @Valid final StudyGoalDTO studyGoalDTO) {
        final Integer createdGoalId = studyGoalService.create(studyGoalDTO);
        return new ResponseEntity<>(createdGoalId, HttpStatus.CREATED);
    }

    @PutMapping("/{goalId}")
    public ResponseEntity<Integer> updateStudyGoal(
            @PathVariable(name = "goalId") final Integer goalId,
            @RequestBody @Valid final StudyGoalDTO studyGoalDTO) {
        studyGoalService.update(goalId, studyGoalDTO);
        return ResponseEntity.ok(goalId);
    }

    @DeleteMapping("/{goalId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteStudyGoal(
            @PathVariable(name = "goalId") final Integer goalId) {
        final ReferencedWarning referencedWarning = studyGoalService.getReferencedWarning(goalId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        studyGoalService.delete(goalId);
        return ResponseEntity.noContent().build();
    }

}
