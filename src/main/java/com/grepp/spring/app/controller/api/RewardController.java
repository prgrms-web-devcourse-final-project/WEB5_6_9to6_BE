package com.grepp.spring.app.controller.api;

import com.grepp.spring.infra.response.CommonResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.annotations.Fetch;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/reward-items", produces = MediaType.APPLICATION_JSON_VALUE)
public class RewardController {

    // 아이템 상점 목록
    @GetMapping
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<Map<String, Object>>> getMockItems() {
        List<Map<String, Object>> items = List.of(
            Map.of("itemId", 1, "name", "블랙테마", "type", "theme", "price", 500),
            Map.of("itemId", 2, "name", "레드테마", "type", "theme", "price", 300),
            Map.of("itemId", 3, "name", "베레모", "type", "hat", "price", 1000),
            Map.of("itemId", 4, "name", "정장", "type", "clothes", "price", 800)
        );

        Map<String, Object> data = Map.of("items", items);
        return ResponseEntity.ok(CommonResponse.success(data));
    }

    // 아이템 구매
    @PostMapping("/{itemId}/purchase")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<Map<String, Object>>> purchaseItem(@PathVariable Long itemId) {
        Map<String, Object> data = Map.of(); // 빈 객체 {}

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CommonResponse.success(data));
    }

    // 소유 아이템 목록
    @GetMapping("/own-items")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<Map<String, Object>>> getOwnItems() {
        Map<String, Object> data = Map.of(
            "member_id", 1,
            "item_id", 1,
            "own_item_id", 1,
            "name", "블랙 테마",
            "type", "테마",
            "is_used", true
        );

        return ResponseEntity
            .ok(CommonResponse.success(data));
    }

    // 사용 아이템 변경
    @PatchMapping("/own-items/{ownItemId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<Map<String, Object>>> changeOwnItems(@PathVariable Long ownItemId) {
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
