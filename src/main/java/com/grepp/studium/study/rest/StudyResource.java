package com.grepp.studium.study.rest;

import com.grepp.studium.study.model.StudyDTO;
import com.grepp.studium.study.service.StudyService;
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
@RequestMapping(value = "/api/studies", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudyResource {

    private final StudyService studyService;

    public StudyResource(final StudyService studyService) {
        this.studyService = studyService;
    }

    @GetMapping
    public ResponseEntity<List<StudyDTO>> getAllStudies() {
        return ResponseEntity.ok(studyService.findAll());
    }

    @GetMapping("/{studyId}")
    public ResponseEntity<StudyDTO> getStudy(
            @PathVariable(name = "studyId") final Integer studyId) {
        return ResponseEntity.ok(studyService.get(studyId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createStudy(@RequestBody @Valid final StudyDTO studyDTO) {
        final Integer createdStudyId = studyService.create(studyDTO);
        return new ResponseEntity<>(createdStudyId, HttpStatus.CREATED);
    }

    @PutMapping("/{studyId}")
    public ResponseEntity<Integer> updateStudy(
            @PathVariable(name = "studyId") final Integer studyId,
            @RequestBody @Valid final StudyDTO studyDTO) {
        studyService.update(studyId, studyDTO);
        return ResponseEntity.ok(studyId);
    }

    @DeleteMapping("/{studyId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteStudy(@PathVariable(name = "studyId") final Integer studyId) {
        final ReferencedWarning referencedWarning = studyService.getReferencedWarning(studyId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        studyService.delete(studyId);
        return ResponseEntity.noContent().build();
    }

}
