package com.grepp.spring.app.controller.api;

import com.grepp.spring.app.controller.api.reward.payload.ImageResponse;
import com.grepp.spring.app.controller.api.reward.payload.SaveImageRequest;
import com.grepp.spring.app.model.auth.domain.Principal;
import com.grepp.spring.app.model.member.service.MemberService;
import com.grepp.spring.app.model.reward.code.ItemType;
import com.grepp.spring.app.model.reward.dto.ItemSetDto;
import com.grepp.spring.app.controller.api.reward.payload.OwnItemResponse;
import com.grepp.spring.app.controller.api.reward.payload.RewardItemResponse;
import com.grepp.spring.app.model.reward.dto.OwnItemDto;
import com.grepp.spring.app.model.reward.dto.RewardItemDto;
import com.grepp.spring.app.model.reward.service.ItemSetService;
import com.grepp.spring.app.model.reward.service.OwnItemService;
import com.grepp.spring.app.model.reward.service.RewardItemService;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.response.SuccessCode;
import com.grepp.spring.infra.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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

@Tag(name = "아이템 API", description = "아이템 구매 및 조회 관련 API입니다.")
@RestController
@RequestMapping(value = "/api/v1/reward-items", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RewardController {

    private final RewardItemService rewardItemService;
    private final OwnItemService ownItemService;
    private final ItemSetService itemSetService;
    private final MemberService memberService;

    // 아이템 상점 목록
    @GetMapping
    @Operation(summary = "아이템 상점 목록", description="전체 아이템 목록을 조회합니다.")
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
    @PostMapping("/{itemId}/purchase")
    @Operation(summary = "아이템 구매", description="ownitem 테이블에 리워드 아이템 정보를 추가합니다. ")
    public ResponseEntity<CommonResponse<Map<String, Object>>> purchaseItem(
         @PathVariable long itemId
    ) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        ownItemService.purchaseItem(memberId,itemId);

        Map<String, Object> data = new HashMap<>();
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CommonResponse.success(data));
    }

    // 소유 아이템 목록
    @GetMapping("/own-items")
    @Operation(summary = "소유 아이템 목록", description="소유한 아이템을 표시합니다.")
    public ResponseEntity<CommonResponse<List<OwnItemResponse>>> getOwnItems() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<OwnItemDto> dtos = ownItemService.getOwnItems(memberId);

         List<OwnItemResponse> responses = dtos.stream()
            .map(OwnItemResponse::from)
            .toList();

         return ResponseEntity.ok(CommonResponse.success(responses));
    }

    // 사용 아이템 변경
    @Operation(summary = "사용 아이템 변경", description="아이템을 변경합니다. 변경 시 서버에 조합된 이미지가 있을 시 프로필 이미지를 반환, 없으면 NO_IMAGE_FOUND 응답이 갑니다.")
    @PatchMapping("/own-items/{ownItemId}")
    public ResponseEntity<CommonResponse<ImageResponse>> changeOwnItems(
        @PathVariable long ownItemId
    ) {

        Long memberId = SecurityUtil.getCurrentMemberId();

        ItemType itemType= ownItemService.changeOwnItems(ownItemId);

        ImageResponse emptyImage = new ImageResponse();

        if (itemType==ItemType.BACKGROUND|| itemType==ItemType.THEME){
            return ResponseEntity.ok(CommonResponse.success(SuccessCode.ITEM_CHANGED_SUCCESSFULLY,emptyImage));
        }



        // 변경 후 현재 사용 중인 아이템들로 조합 이미지 체크
        ItemSetDto itemSetDto = ownItemService.getUseItemList(memberId);
        Optional<ImageResponse> imageOpt = itemSetService.ExistItemSet(itemSetDto);

        if (imageOpt.isPresent()) {
            ImageResponse image = imageOpt.get();

            // 3. 조합 이미지 있으면 → 멤버 테이블 업데이트
            memberService.updateProfileImage(memberId, image.getImage());

            // 4. 이미지 포함 응답
            return ResponseEntity.ok(CommonResponse.success(image));
        } else {
            // 5. 이미지 없으면 → 멤버 테이블 업데이트 생략 + 메시지 응답
            return ResponseEntity.ok(CommonResponse.noContent(SuccessCode.NO_IMAGE_FOUND));
        }
    }


    @GetMapping("/{itemId}/image")
    @ApiResponse(responseCode = "200")
    @Operation(summary = "서버에 조합된 이미지 있는지 없는지 판단", description="서버에 조합된 이미지가 있는지 없는지 판단합니다. \n 아이템 변경에서 같은 로직을 수행하기 때문에 안쓰셔도 됩니다.")
    public ResponseEntity<CommonResponse<ImageResponse>> getItemImages(@PathVariable Long itemId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        ItemSetDto itemSetDto = ownItemService.getUseItemList(memberId);
        Optional<ImageResponse> image = itemSetService.ExistItemSet(itemSetDto);

        return image.map(img ->
            ResponseEntity.ok(CommonResponse.success(img))  // 조합 존재 → 이미지 반환
        ).orElseGet(() ->
            ResponseEntity.ok(CommonResponse.noContent(SuccessCode.NO_IMAGE_FOUND)) // 조합 없음
        );
    }

    @PostMapping("/saveimage")
    @ApiResponse(responseCode = "200")
    @Operation(summary = "서버에 이미지 저장", description="서버에 해당 조합에 해당하는 조합과 이미지를 가진 데이터를 생성합니다.")
    public ResponseEntity<CommonResponse<Map<String, Object>>> PostItemImages(
        @Valid @RequestBody SaveImageRequest saveImageRequest) {
        Map<String, Object> data = Map.of();

        itemSetService.saveImage(saveImageRequest);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CommonResponse.success(data));
    }



}
