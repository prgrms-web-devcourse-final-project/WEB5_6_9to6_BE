package com.grepp.studium.reward_item.rest;

import com.grepp.studium.reward_item.model.RewardItemDTO;
import com.grepp.studium.reward_item.service.RewardItemService;
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
@RequestMapping(value = "/api/rewardItems", produces = MediaType.APPLICATION_JSON_VALUE)
public class RewardItemResource {

    private final RewardItemService rewardItemService;

    public RewardItemResource(final RewardItemService rewardItemService) {
        this.rewardItemService = rewardItemService;
    }

    @GetMapping
    public ResponseEntity<List<RewardItemDTO>> getAllRewardItems() {
        return ResponseEntity.ok(rewardItemService.findAll());
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<RewardItemDTO> getRewardItem(
            @PathVariable(name = "itemId") final Integer itemId) {
        return ResponseEntity.ok(rewardItemService.get(itemId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createRewardItem(
            @RequestBody @Valid final RewardItemDTO rewardItemDTO) {
        final Integer createdItemId = rewardItemService.create(rewardItemDTO);
        return new ResponseEntity<>(createdItemId, HttpStatus.CREATED);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<Integer> updateRewardItem(
            @PathVariable(name = "itemId") final Integer itemId,
            @RequestBody @Valid final RewardItemDTO rewardItemDTO) {
        rewardItemService.update(itemId, rewardItemDTO);
        return ResponseEntity.ok(itemId);
    }

    @DeleteMapping("/{itemId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRewardItem(
            @PathVariable(name = "itemId") final Integer itemId) {
        final ReferencedWarning referencedWarning = rewardItemService.getReferencedWarning(itemId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        rewardItemService.delete(itemId);
        return ResponseEntity.noContent().build();
    }

}
