package com.grepp.spring.app.model.reward.repository;


import com.grepp.spring.app.model.reward.code.ItemType;
import com.grepp.spring.app.model.reward.entity.OwnItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnItemRepository extends JpaRepository<OwnItem,Long>, OwnItemRepositoryCustom {

    Optional<OwnItem> findFirstByRewardItem_ItemTypeAndIsUsedTrue(ItemType itemType);


}
