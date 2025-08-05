package com.grepp.spring.app.model.reward.dto.internal;

import com.grepp.spring.app.model.reward.code.ItemType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class OwnItemDto {


    private final Long itemId;
    private final Long ownItemId;
    private final String name;
    private final ItemType itemtype;
    private final boolean isUsed;

    @QueryProjection
    public OwnItemDto(Long itemId, Long ownItemId, String name, ItemType itemtype, boolean isUsed) {
        this.itemId = itemId;
        this.ownItemId = ownItemId;
        this.name = name;
        this.itemtype = itemtype;
        this.isUsed = isUsed;
    }

}
