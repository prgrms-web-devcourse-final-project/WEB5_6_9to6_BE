package com.grepp.studium.timer.rest;

import com.grepp.studium.timer.model.TimerDTO;
import com.grepp.studium.timer.service.TimerService;
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
@RequestMapping(value = "/api/timers", produces = MediaType.APPLICATION_JSON_VALUE)
public class TimerResource {

    private final TimerService timerService;

    public TimerResource(final TimerService timerService) {
        this.timerService = timerService;
    }

    @GetMapping
    public ResponseEntity<List<TimerDTO>> getAllTimers() {
        return ResponseEntity.ok(timerService.findAll());
    }

    @GetMapping("/{timerId}")
    public ResponseEntity<TimerDTO> getTimer(
            @PathVariable(name = "timerId") final Integer timerId) {
        return ResponseEntity.ok(timerService.get(timerId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createTimer(@RequestBody @Valid final TimerDTO timerDTO) {
        final Integer createdTimerId = timerService.create(timerDTO);
        return new ResponseEntity<>(createdTimerId, HttpStatus.CREATED);
    }

    @PutMapping("/{timerId}")
    public ResponseEntity<Integer> updateTimer(
            @PathVariable(name = "timerId") final Integer timerId,
            @RequestBody @Valid final TimerDTO timerDTO) {
        timerService.update(timerId, timerDTO);
        return ResponseEntity.ok(timerId);
    }

    @DeleteMapping("/{timerId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTimer(@PathVariable(name = "timerId") final Integer timerId) {
        timerService.delete(timerId);
        return ResponseEntity.noContent().build();
    }

}
