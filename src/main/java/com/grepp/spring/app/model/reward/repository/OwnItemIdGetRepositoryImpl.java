package com.grepp.spring.app.model.reward.repository;

import com.grepp.spring.app.model.reward.code.ItemType;
import com.grepp.spring.app.model.reward.entity.QOwnItem;
import com.grepp.spring.app.model.reward.entity.QRewardItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OwnItemIdGetRepositoryImpl implements OwnItemIdGetRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final QOwnItem ownItem = QOwnItem.ownItem;
    private static final QRewardItem rewardItem = QRewardItem.rewardItem;

    @Override
    public List<Long> findIdsByMemberId(Long memberId) {
        return queryFactory
            .select(ownItem.rewardItem.itemId)
            .from(ownItem)
            .join(ownItem.rewardItem, rewardItem)
            .where(
                ownItem.isUsed.isTrue(),
                ownItem.memberId.eq(memberId),
                rewardItem.itemType.in(ItemType.HAT, ItemType.HAIR, ItemType.FACE, ItemType.TOP),
                rewardItem.activated.isTrue()
            )
            .fetch();
    }
}
