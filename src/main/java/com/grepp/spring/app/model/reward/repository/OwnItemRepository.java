package com.grepp.spring.app.model.reward.repository;




import com.grepp.spring.app.model.reward.code.ItemType;
import com.grepp.spring.app.model.reward.entity.OwnItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.grepp.spring.app.model.reward.dto.internal.OwnItemDto;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnItemRepository extends JpaRepository<OwnItem,Long>, OwnItemRepositoryCustom {


    Optional<OwnItem> findFirstByRewardItem_ItemTypeAndIsUsedTrue(ItemType itemType);
    List<OwnItem> findByMemberIdAndIsUsedTrue(Long memberId);
    List<OwnItemDto> findOwnItemsByMemberId(Long memberId);
    boolean existsByMemberIdAndRewardItem_ItemId(Long MemberId, Long itemId);
    List<OwnItem> findByRewardItem_ItemTypeAndIsUsedTrue(ItemType itemType);


}
