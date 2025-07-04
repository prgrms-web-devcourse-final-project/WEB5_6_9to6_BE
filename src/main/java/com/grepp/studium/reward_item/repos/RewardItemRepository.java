package com.grepp.studium.reward_item.repos;

import com.grepp.studium.reward_item.domain.RewardItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RewardItemRepository extends JpaRepository<RewardItem, Integer> {
}
