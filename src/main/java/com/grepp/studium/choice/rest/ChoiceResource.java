package com.grepp.studium.choice.rest;

import com.grepp.studium.choice.model.ChoiceDTO;
import com.grepp.studium.choice.service.ChoiceService;
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
@RequestMapping(value = "/api/choices", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChoiceResource {

    private final ChoiceService choiceService;

    public ChoiceResource(final ChoiceService choiceService) {
        this.choiceService = choiceService;
    }

    @GetMapping
    public ResponseEntity<List<ChoiceDTO>> getAllChoices() {
        return ResponseEntity.ok(choiceService.findAll());
    }

    @GetMapping("/{choiceId}")
    public ResponseEntity<ChoiceDTO> getChoice(
            @PathVariable(name = "choiceId") final Integer choiceId) {
        return ResponseEntity.ok(choiceService.get(choiceId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createChoice(@RequestBody @Valid final ChoiceDTO choiceDTO) {
        final Integer createdChoiceId = choiceService.create(choiceDTO);
        return new ResponseEntity<>(createdChoiceId, HttpStatus.CREATED);
    }

    @PutMapping("/{choiceId}")
    public ResponseEntity<Integer> updateChoice(
            @PathVariable(name = "choiceId") final Integer choiceId,
            @RequestBody @Valid final ChoiceDTO choiceDTO) {
        choiceService.update(choiceId, choiceDTO);
        return ResponseEntity.ok(choiceId);
    }

    @DeleteMapping("/{choiceId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteChoice(
            @PathVariable(name = "choiceId") final Integer choiceId) {
        choiceService.delete(choiceId);
        return ResponseEntity.noContent().build();
    }

}
