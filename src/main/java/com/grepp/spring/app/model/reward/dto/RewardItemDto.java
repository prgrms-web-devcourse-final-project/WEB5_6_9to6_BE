package com.grepp.spring.app.model.reward.dto;

import com.grepp.spring.app.model.reward.code.ItemType;
import com.grepp.spring.app.model.reward.entity.RewardItem;

public record RewardItemDto(
    Long itemId,
    String name,
    int price,
    ItemType itemtype

) {

    public static RewardItemDto fromEntity(RewardItem rewardItem) {
        return new RewardItemDto(
            rewardItem.getItemId(),
            rewardItem.getName(),
            rewardItem.getPrice(),
            rewardItem.getItemType()
        );
    }

}


