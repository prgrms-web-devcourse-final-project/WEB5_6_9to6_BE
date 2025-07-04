package com.grepp.studium.quiz_set.rest;

import com.grepp.studium.quiz_set.model.QuizSetDTO;
import com.grepp.studium.quiz_set.service.QuizSetService;
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
@RequestMapping(value = "/api/quizSets", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuizSetResource {

    private final QuizSetService quizSetService;

    public QuizSetResource(final QuizSetService quizSetService) {
        this.quizSetService = quizSetService;
    }

    @GetMapping
    public ResponseEntity<List<QuizSetDTO>> getAllQuizSets() {
        return ResponseEntity.ok(quizSetService.findAll());
    }

    @GetMapping("/{quizSetId}")
    public ResponseEntity<QuizSetDTO> getQuizSet(
            @PathVariable(name = "quizSetId") final Integer quizSetId) {
        return ResponseEntity.ok(quizSetService.get(quizSetId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createQuizSet(@RequestBody @Valid final QuizSetDTO quizSetDTO) {
        final Integer createdQuizSetId = quizSetService.create(quizSetDTO);
        return new ResponseEntity<>(createdQuizSetId, HttpStatus.CREATED);
    }

    @PutMapping("/{quizSetId}")
    public ResponseEntity<Integer> updateQuizSet(
            @PathVariable(name = "quizSetId") final Integer quizSetId,
            @RequestBody @Valid final QuizSetDTO quizSetDTO) {
        quizSetService.update(quizSetId, quizSetDTO);
        return ResponseEntity.ok(quizSetId);
    }

    @DeleteMapping("/{quizSetId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteQuizSet(
            @PathVariable(name = "quizSetId") final Integer quizSetId) {
        final ReferencedWarning referencedWarning = quizSetService.getReferencedWarning(quizSetId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        quizSetService.delete(quizSetId);
        return ResponseEntity.noContent().build();
    }

}
