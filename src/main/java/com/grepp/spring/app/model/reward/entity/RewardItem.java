package com.grepp.spring.app.model.reward.entity;

import com.grepp.spring.app.model.reward.code.ItemType;
import com.grepp.spring.app.model.reward.code.ItemTypeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reward_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RewardItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Convert(converter = ItemTypeConverter.class)
    private ItemType itemType;

    @Column(nullable = false)
    private boolean activated;
    private String image;

    @Builder
    public RewardItem(Long itemId, String name, int price, ItemType itemType, boolean activated,
        String image) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.itemType = itemType;
        this.activated = activated;
        this.image = image;
    }



}
