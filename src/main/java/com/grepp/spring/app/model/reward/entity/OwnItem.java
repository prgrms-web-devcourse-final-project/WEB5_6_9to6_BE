package com.grepp.spring.app.model.reward.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "own_item")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OwnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ownItemId;

    private Long memberId;

    @NotNull
    private boolean isUsed;

    @NotNull
    private boolean activated;

    @ManyToOne
    @JoinColumn(name = "reward_item")
    private RewardItem rewardItem;

    public void use(boolean used) {
        this.isUsed = used;
    }

}
