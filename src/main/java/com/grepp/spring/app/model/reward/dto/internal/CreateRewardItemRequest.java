package com.grepp.spring.app.model.reward.dto.internal;

import com.grepp.spring.app.model.reward.code.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRewardItemRequest {

    @NotBlank
    private String name;

    @NotNull
    private Integer price;

    @NotNull
    private ItemType itemType; // 예: HAT, TOP, etc.

    private Boolean activated = true; // 기본값 true (생략 가능)
}
