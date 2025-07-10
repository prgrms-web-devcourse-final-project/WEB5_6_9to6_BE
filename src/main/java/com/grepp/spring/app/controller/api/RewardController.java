package com.grepp.spring.app.controller.api;


import com.grepp.spring.app.controller.api.reward.payload.PurchaseRequest;
import com.grepp.spring.app.controller.api.reward.payload.OwnItemResponse;
import com.grepp.spring.app.controller.api.reward.payload.RewardItemResponseDto;
import com.grepp.spring.app.model.reward.dto.OwnItemDto;
import com.grepp.spring.app.model.reward.dto.RewardItemDto;
import com.grepp.spring.app.model.reward.service.OwnItemService;
import com.grepp.spring.app.model.reward.service.RewardItemService;
import com.grepp.spring.infra.response.CommonResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/reward-items", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RewardController {

    private final RewardItemService rewardItemService;
    private final OwnItemService ownItemService;

    // 아이템 상점 목록
    @GetMapping
    public ResponseEntity<CommonResponse<RewardItemResponseDto>> getRewardItems() {
List<RewardItemDto> dtos = rewardItemService.getItemList();
        RewardItemResponseDto responseDto = new RewardItemResponseDto(dtos);

  return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    // 아이템 구매
    @PostMapping("/{itemId}/purchase")
    public ResponseEntity<CommonResponse<Map<String, Object>>> purchaseItem(
        @PathVariable long itemId,
        @AuthenticationPrincipal User userDetails
    ) {
        long userId = Long.parseLong(userDetails.getUsername());

        ownItemService.purchaseItem(userId,itemId);


        Map<String, Object> data = new HashMap<>();
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CommonResponse.success(data));
    }

    // 소유 아이템 목록
    @GetMapping("/own-items")
    public ResponseEntity<CommonResponse<List<OwnItemResponse>>> getOwnItems(@AuthenticationPrincipal User userDetails) {
        Long memberId = Long.valueOf(userDetails.getUsername()); // 실제 로그인 유저 ID 사용
        List<OwnItemDto> dtos = ownItemService.getOwnItems(memberId);

         List<OwnItemResponse> responses = dtos.stream()
            .map(OwnItemResponse::from)
            .toList();

         return ResponseEntity.ok(CommonResponse.success(responses));
    }

    // 사용 아이템 변경
    @PatchMapping("/own-items/{ownItemId}")
    public ResponseEntity<CommonResponse<Map<String, Object>>> changeOwnItems(
        @PathVariable long ownItemId
    ) {
        Map<String, Object> data = Map.of();

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CommonResponse.success(data));
    }

    @PatchMapping("/{itemId}/image")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> getItemImages(@PathVariable Long itemId) {
        Map<String, Object> response = new LinkedHashMap<>();


        // 짝수일 경우 서버에 이미지 존재
        if (itemId% 2==0) {

            response.put("code", "0000");
            response.put("message", "이미지를 조회했습니다.");
            Map<String, Object> data;
            data = Map.of("wholeImageUrl", "https://example.com/images/whole-outfit-" + itemId + ".png");

            response.put("data", data);
            return ResponseEntity.ok(response);
        } else { // 홀수 일 때 서버에 이미지 없음

            response.put("code", "0001");
            response.put("message", "서버에 이미지가 없습니다.");

            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/saveimage")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<Map<String, Object>>> PostItemImages(
        @RequestBody(required = false) Map<String, Object> PostItemRequest) {
        Map<String, Object> data = Map.of();

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CommonResponse.success(data));
    }





}
