package com.grepp.studium.own_item.rest;

import com.grepp.studium.own_item.model.OwnItemDTO;
import com.grepp.studium.own_item.service.OwnItemService;
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
@RequestMapping(value = "/api/ownItems", produces = MediaType.APPLICATION_JSON_VALUE)
public class OwnItemResource {

    private final OwnItemService ownItemService;

    public OwnItemResource(final OwnItemService ownItemService) {
        this.ownItemService = ownItemService;
    }

    @GetMapping
    public ResponseEntity<List<OwnItemDTO>> getAllOwnItems() {
        return ResponseEntity.ok(ownItemService.findAll());
    }

    @GetMapping("/{ownItemId}")
    public ResponseEntity<OwnItemDTO> getOwnItem(
            @PathVariable(name = "ownItemId") final Integer ownItemId) {
        return ResponseEntity.ok(ownItemService.get(ownItemId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createOwnItem(@RequestBody @Valid final OwnItemDTO ownItemDTO) {
        final Integer createdOwnItemId = ownItemService.create(ownItemDTO);
        return new ResponseEntity<>(createdOwnItemId, HttpStatus.CREATED);
    }

    @PutMapping("/{ownItemId}")
    public ResponseEntity<Integer> updateOwnItem(
            @PathVariable(name = "ownItemId") final Integer ownItemId,
            @RequestBody @Valid final OwnItemDTO ownItemDTO) {
        ownItemService.update(ownItemId, ownItemDTO);
        return ResponseEntity.ok(ownItemId);
    }

    @DeleteMapping("/{ownItemId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOwnItem(
            @PathVariable(name = "ownItemId") final Integer ownItemId) {
        ownItemService.delete(ownItemId);
        return ResponseEntity.noContent().build();
    }

}
