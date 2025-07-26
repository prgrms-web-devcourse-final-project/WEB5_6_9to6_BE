package com.grepp.spring.app.model.member.repository;

import static com.grepp.spring.app.model.member.entity.QMember.member;

import com.grepp.spring.app.model.member.dto.response.RequiredMemberInfoResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public Long findIdByEmail(String email) {
        return queryFactory
            .select(member.id)
            .from(member)
            .where(member.email.eq(email))
            .fetchOne();
    }

    @Override
    public String getNickname(Long id) {
        return queryFactory
            .select(member.nickname)
            .from(member)
            .where(member.id.eq(id))
            .fetchOne();
    }

    @Override
    public RequiredMemberInfoResponse getRequiredMemberInfo(Long memberId) {
        return queryFactory
            .select(Projections.constructor(
                RequiredMemberInfoResponse.class,
                member.id,
                member.email,
                member.nickname,
                member.birthday,
                member.gender,
                member.rewardPoints,
                member.winCount,
                member.socialType,
                member.role
            ))
            .from(member)
            .where(member.activated.isTrue()
                .and(member.id.eq(memberId)))
            .fetchOne();
    }

    @Override
    public String getAvatarImage(Long memberId) {
        return queryFactory
            .select(member.avatarImage)
            .from(member)
            .where(member.id.eq(memberId))
            .fetchOne();
    }

    @Override
    @Transactional
    public void addRewardPoints(Long memberId, int points) {
        queryFactory
            .update(member)
            .set(member.rewardPoints, member.rewardPoints.add(points))
            .where(member.id.eq(memberId))
            .execute();
        em.flush();
        em.clear();
    }
}

