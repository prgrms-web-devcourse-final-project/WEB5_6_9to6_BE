package com.grepp.spring.app.model.reward.repository;

import com.grepp.spring.app.model.reward.entity.RewardItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardItemRepository extends JpaRepository<RewardItem,Long> {

}
