package com.grepp.spring.app.controller.api.reward.payload;

public record OwnItemRequestDto(
    Long memberId,
    boolean isUsed,
    boolean activated,
    Long rewardItemId

) {

}
