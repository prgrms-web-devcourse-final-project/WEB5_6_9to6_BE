package com.grepp.studium.own_item.repos;

import com.grepp.studium.member.domain.Member;
import com.grepp.studium.own_item.domain.OwnItem;
import com.grepp.studium.reward_item.domain.RewardItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OwnItemRepository extends JpaRepository<OwnItem, Integer> {

    OwnItem findFirstByMember(Member member);

    OwnItem findFirstByItem(RewardItem rewardItem);

}
