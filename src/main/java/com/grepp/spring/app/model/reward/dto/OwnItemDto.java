package com.grepp.spring.app.model.reward.dto;

import com.grepp.spring.app.model.reward.code.ItemType;
import com.querydsl.core.annotations.QueryProjection;

public class OwnItemDto {


    private Long itemId;
    private Long ownItemId;
    private String name;
    private ItemType itemtype;
    private boolean isUsed;

    @QueryProjection
    public OwnItemDto(Long itemId, Long ownItemId, String name, ItemType itemtype, boolean isUsed) {
        this.itemId = itemId;
        this.ownItemId = ownItemId;
        this.name = name;
        this.itemtype = itemtype;
        this.isUsed = isUsed;
    }

}
