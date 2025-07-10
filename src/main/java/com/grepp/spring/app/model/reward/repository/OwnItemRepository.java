package com.grepp.spring.app.model.reward.repository;




import com.grepp.spring.app.model.reward.code.ItemType;
import com.grepp.spring.app.model.reward.entity.OwnItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.grepp.spring.app.model.reward.dto.OwnItemDto;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnItemRepository extends JpaRepository<OwnItem,Long>, OwnItemRepositoryCustom {


    Optional<OwnItem> findFirstByRewardItem_ItemTypeAndIsUsedTrue(ItemType itemType);
    List<OwnItem> findByMemberIdAndIsUsedTrue(Long memberId);
    List<OwnItemDto> findOwnItemsByMemberId(Long memberId);


}
