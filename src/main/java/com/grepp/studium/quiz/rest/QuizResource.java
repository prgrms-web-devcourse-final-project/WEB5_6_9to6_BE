package com.grepp.studium.quiz.rest;

import com.grepp.studium.quiz.model.QuizDTO;
import com.grepp.studium.quiz.service.QuizService;
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
@RequestMapping(value = "/api/quizzes", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuizResource {

    private final QuizService quizService;

    public QuizResource(final QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public ResponseEntity<List<QuizDTO>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.findAll());
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDTO> getQuiz(@PathVariable(name = "quizId") final Integer quizId) {
        return ResponseEntity.ok(quizService.get(quizId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createQuiz(@RequestBody @Valid final QuizDTO quizDTO) {
        final Integer createdQuizId = quizService.create(quizDTO);
        return new ResponseEntity<>(createdQuizId, HttpStatus.CREATED);
    }

    @PutMapping("/{quizId}")
    public ResponseEntity<Integer> updateQuiz(@PathVariable(name = "quizId") final Integer quizId,
            @RequestBody @Valid final QuizDTO quizDTO) {
        quizService.update(quizId, quizDTO);
        return ResponseEntity.ok(quizId);
    }

    @DeleteMapping("/{quizId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteQuiz(@PathVariable(name = "quizId") final Integer quizId) {
        final ReferencedWarning referencedWarning = quizService.getReferencedWarning(quizId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        quizService.delete(quizId);
        return ResponseEntity.noContent().build();
    }

}
