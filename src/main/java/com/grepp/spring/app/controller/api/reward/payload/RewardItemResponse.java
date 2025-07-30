package com.grepp.spring.app.controller.api.reward.payload;

import com.grepp.spring.app.model.reward.dto.RewardItemDto;
import java.util.List;

public record RewardItemResponse(
    List<RewardItemDto> items
) {

}
