package com.grepp.spring.app.controller.api;

import com.grepp.spring.app.controller.api.reward.payload.ImageResponse;
import com.grepp.spring.app.controller.api.reward.payload.OwnItemResponse;
import com.grepp.spring.app.controller.api.reward.payload.RewardItemResponse;
import com.grepp.spring.app.controller.api.reward.payload.SaveImageRequest;
import com.grepp.spring.app.model.auth.domain.Principal;
import com.grepp.spring.app.model.reward.dto.ItemSetDto;
import com.grepp.spring.app.model.reward.dto.OwnItemDto;
import com.grepp.spring.app.model.reward.dto.RewardItemDto;
import com.grepp.spring.app.model.reward.service.ItemSetService;
import com.grepp.spring.app.model.reward.service.OwnItemService;
import com.grepp.spring.app.model.reward.service.RewardItemService;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.response.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "리워드(아이템) API", description = "아이템 상점, 구매, 소유 아이템 관리 관련 API 입니다.")
@RestController
@RequestMapping(value = "/api/v1/reward-items", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RewardController {

    private final RewardItemService rewardItemService;
    private final OwnItemService ownItemService;
    private final ItemSetService itemSetService;

    // 아이템 상점 목록
    @Operation(summary = "아이템 상점 목록 조회", description = "아이템 상점에서 현재 판매 중인 모든 아이템의 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonResponse<RewardItemResponse>> getRewardItems() {
        List<RewardItemDto> dtos = rewardItemService.getItemList();
            RewardItemResponse responseDto = new RewardItemResponse(dtos);

        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

//    @GetMapping
//    public ResponseEntity<CommonResponse<Page<RewardItemDto>>> getRewardItems(
//        @PageableDefault(size = 10) Pageable pageable
//    ) {
//        Page<RewardItemDto> responsePage = rewardItemService.getItemList(pageable);
//        return ResponseEntity.ok(CommonResponse.success(responsePage));
//    }

    // 아이템 구매
    @Operation(summary = "아이템 구매", description = """
        `itemId`에 해당하는 아이템을 구매합니다.
        - 이 API는 인증이 필요하며, 요청 헤더에 유효한 토큰이 있어야 합니다.
        - 구매 성공 시, 사용자의 포인트가 차감되고 소유 아이템 목록에 추가됩니다.
        """)
    @PostMapping("/{itemId}/purchase")
    public ResponseEntity<CommonResponse<Map<String, Object>>> purchaseItem(
         @PathVariable long itemId,
        Authentication authentication
    ) {
        Principal principal = (Principal) authentication.getPrincipal();
        long memberId = principal.getMemberId();

        ownItemService.purchaseItem(memberId,itemId);

        Map<String, Object> data = new HashMap<>();
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CommonResponse.success(data));
    }

    // 소유 아이템 목록
    @Operation(summary = "소유 아이템 목록 조회", description = """
        현재 로그인한 사용자가 구매하여 소유하고 있는 모든 아이템의 목록을 조회합니다.
        - 각 아이템의 착용 여부(`is_used`)도 함께 반환됩니다.
        - 이 API는 인증이 필요하며, 요청 헤더에 유효한 토큰이 있어야 합니다.
        """)
    @GetMapping("/own-items")
    public ResponseEntity<CommonResponse<List<OwnItemResponse>>> getOwnItems(
        Authentication authentication) {

        Principal principal = (Principal) authentication.getPrincipal();
        long memberId = principal.getMemberId(); // 실제 로그인 유저 ID 사용
        List<OwnItemDto> dtos = ownItemService.getOwnItems(memberId);

         List<OwnItemResponse> responses = dtos.stream()
            .map(OwnItemResponse::from)
            .toList();

         return ResponseEntity.ok(CommonResponse.success(responses));
    }

    // 사용 아이템 변경
    @Operation(summary = "사용 아이템 변경", description = """
        소유 아이템 ID(`ownItemId`)를 받아 해당 아이템의 착용 상태를 변경합니다.
        """)
    @PatchMapping("/own-items/{ownItemId}")
    public ResponseEntity<CommonResponse<Map<String, Object>>> changeOwnItems(
        @PathVariable long ownItemId
    ) {
        Map<String, Object> data = Map.of();

        ownItemService.changeOwnItems(ownItemId);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CommonResponse.success(data));
    }

    @Operation(summary = "현재 착용 중인 아이템 조합 이미지 조회", description = """
        현재 로그인한 사용자가 착용하고 있는 아이템 조합에 해당하는 이미지가 있는지 조회합니다.
        - 조합된 이미지가 존재하면 이미지 경로를 반환하고, 없으면 데이터 없이 성공 코드를 반환합니다.
        - 이 API는 인증이 필요하며, 요청 헤더에 유효한 토큰이 있어야 합니다.
        """)

    @GetMapping("/{itemId}/image")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<ImageResponse>> getItemImages(@PathVariable Long itemId,
        Authentication authentication) {
        Principal principal = (Principal) authentication.getPrincipal();
        long memberId = principal.getMemberId(); // 실제 로그인 유저 ID 사용

        ItemSetDto itemSetDto = ownItemService.getUseItemList(memberId);
        Optional<ImageResponse> image = itemSetService.ExistItemSet(itemSetDto);

        return image.map(img ->
            ResponseEntity.ok(CommonResponse.success(img))  // 조합 존재 → 이미지 반환
        ).orElseGet(() ->
            ResponseEntity.ok(CommonResponse.noContent(SuccessCode.NO_IMAGE_FOUND)) // 조합 없음
        );
    }

    @Operation(summary = "아이템 조합 이미지 정보 저장 (내부용)", description = """
        요청 body에 `SaveImageRequest`를 포함해야합니다.
        - 특정 아이템 조합과 그에 해당하는 이미지 URL을 시스템에 저장합니다. 주로 관리자나 내부 시스템에서 사용됩니다.
        """)
    @PostMapping("/saveimage")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<Map<String, Object>>> PostItemImages(
        @Valid @RequestBody SaveImageRequest saveImageRequest) {
        Map<String, Object> data = Map.of();

        itemSetService.saveImage(saveImageRequest);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CommonResponse.success(data));
    }





}
