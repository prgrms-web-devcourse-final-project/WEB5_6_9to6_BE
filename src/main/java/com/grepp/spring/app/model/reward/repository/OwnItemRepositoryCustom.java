package com.grepp.spring.app.model.reward.repository;

import com.grepp.spring.app.model.reward.code.ItemType;
import com.grepp.spring.app.model.reward.dto.OwnItemDto;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnItemRepositoryCustom {
    List<OwnItemDto> findOwnItemsByMemberId(Long memberId);

    long bulkUnsetUsedItemsByType(Long memberId, ItemType itemType);
}
