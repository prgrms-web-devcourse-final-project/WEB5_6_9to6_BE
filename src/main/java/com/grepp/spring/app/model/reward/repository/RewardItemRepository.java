package com.grepp.spring.app.model.reward.repository;

import com.grepp.spring.app.model.reward.entity.RewardItem;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardItemRepository extends JpaRepository<RewardItem,Long> {
    List<RewardItem> findAllByItemIdIn(List<Long> itemId);
    Page<RewardItem> findAllByActivatedIsTrue(Pageable pageable);
}
