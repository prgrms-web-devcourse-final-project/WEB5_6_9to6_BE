package com.grepp.spring.app.model.reward.dto;

import com.grepp.spring.app.model.reward.code.ItemType;

public record OwnItemDto(
    Long itemId,
    Long ownItemId,
    String name ,
    ItemType itemtype,
    boolean isUsed
) {

}
