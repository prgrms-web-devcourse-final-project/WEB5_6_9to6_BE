package com.grepp.spring.app.model.reward.code;


public enum ItemType {
    BACKGROUND,
    HAT,
    HAIR,
    FACE,
    TOP;

    public static ItemType from(String value) {
        return ItemType.valueOf(value.toUpperCase());
    }
}
