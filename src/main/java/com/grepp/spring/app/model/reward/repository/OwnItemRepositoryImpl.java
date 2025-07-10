package com.grepp.spring.app.model.reward.repository;


import com.grepp.spring.app.model.reward.dto.OwnItemDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public interface OwnItemRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    public List<OwnItemDto> findOwnItemsByMemberId(Long memberId) {
        QOwnItem o = QOwnItem.ownItem;
        QRewardItem r = QRewardItem.rewardItem;

        return queryFactory
            .select(new QOwnItemDto(
                r.itemId,
                o.ownItemId,
                r.name,
                r.itemtype,
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
}
