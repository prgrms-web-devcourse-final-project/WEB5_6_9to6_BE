package com.grepp.spring.app.model.reward.dto.response;

import com.grepp.spring.app.model.reward.dto.internal.RewardItemDto;
import java.util.List;

public record RewardItemResponse(
    List<RewardItemDto> items
) {

}
