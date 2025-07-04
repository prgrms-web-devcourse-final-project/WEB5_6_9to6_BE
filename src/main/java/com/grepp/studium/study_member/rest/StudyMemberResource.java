package com.grepp.studium.study_member.rest;

import com.grepp.studium.study_member.model.StudyMemberDTO;
import com.grepp.studium.study_member.service.StudyMemberService;
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
@RequestMapping(value = "/api/studyMembers", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudyMemberResource {

    private final StudyMemberService studyMemberService;

    public StudyMemberResource(final StudyMemberService studyMemberService) {
        this.studyMemberService = studyMemberService;
    }

    @GetMapping
    public ResponseEntity<List<StudyMemberDTO>> getAllStudyMembers() {
        return ResponseEntity.ok(studyMemberService.findAll());
    }

    @GetMapping("/{studyMemberId}")
    public ResponseEntity<StudyMemberDTO> getStudyMember(
            @PathVariable(name = "studyMemberId") final Integer studyMemberId) {
        return ResponseEntity.ok(studyMemberService.get(studyMemberId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createStudyMember(
            @RequestBody @Valid final StudyMemberDTO studyMemberDTO) {
        final Integer createdStudyMemberId = studyMemberService.create(studyMemberDTO);
        return new ResponseEntity<>(createdStudyMemberId, HttpStatus.CREATED);
    }

    @PutMapping("/{studyMemberId}")
    public ResponseEntity<Integer> updateStudyMember(
            @PathVariable(name = "studyMemberId") final Integer studyMemberId,
            @RequestBody @Valid final StudyMemberDTO studyMemberDTO) {
        studyMemberService.update(studyMemberId, studyMemberDTO);
        return ResponseEntity.ok(studyMemberId);
    }

    @DeleteMapping("/{studyMemberId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteStudyMember(
            @PathVariable(name = "studyMemberId") final Integer studyMemberId) {
        final ReferencedWarning referencedWarning = studyMemberService.getReferencedWarning(studyMemberId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        studyMemberService.delete(studyMemberId);
        return ResponseEntity.noContent().build();
    }

}
