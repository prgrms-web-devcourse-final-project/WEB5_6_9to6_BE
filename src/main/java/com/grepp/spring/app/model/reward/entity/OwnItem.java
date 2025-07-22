package com.grepp.spring.app.model.reward.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "own_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OwnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ownItemId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private boolean isUsed;

    @Column(nullable = false)
    private boolean activated;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private RewardItem rewardItem;


    @Builder
    public OwnItem(Long ownItemId, Long memberId, boolean isUsed, boolean activated,
        RewardItem rewardItem) {
        this.ownItemId = ownItemId;
        this.memberId = memberId;
        this.isUsed = isUsed;
        this.activated = activated;
        this.rewardItem = rewardItem;
    }

    public void use(boolean used) {
        this.isUsed = used;
    }

}
