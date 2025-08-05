package com.grepp.spring.app.model.reward.repository;


import com.grepp.spring.app.model.reward.code.ItemType;
import com.grepp.spring.app.model.reward.dto.internal.OwnItemDto;
import com.grepp.spring.app.model.reward.dto.QOwnItemDto;
import com.grepp.spring.app.model.reward.entity.QOwnItem;
import com.grepp.spring.app.model.reward.entity.QRewardItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class OwnItemRepositoryImpl implements OwnItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<OwnItemDto> findOwnItemsByMemberId(Long memberId) {
        QOwnItem o = QOwnItem.ownItem;
        QRewardItem r = QRewardItem.rewardItem;

        return queryFactory
            .select(new QOwnItemDto(
                r.itemId,
                o.ownItemId,
                r.name,
                r.itemType,
                o.isUsed
            ))
            .from(o)
            .join(o.rewardItem, r)
            .where(
                o.memberId.eq(memberId),
                o.activated.isTrue(),
                r.activated.isTrue()
            )
            .fetch();
    }
    public long bulkUnsetUsedItemsByType(Long memberId, ItemType itemType) {
        QOwnItem ownItem = QOwnItem.ownItem;
        return queryFactory.update(ownItem)
            .set(ownItem.isUsed, false)
            .where(
                ownItem.isUsed.eq(true),
                ownItem.rewardItem.itemType.eq(itemType),
                ownItem.memberId.eq(memberId)
            )
            .execute();
    }


}
