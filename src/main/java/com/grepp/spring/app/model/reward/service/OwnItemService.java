package com.grepp.spring.app.model.reward.service;


import com.grepp.spring.app.model.member.MemberRepository;
import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.reward.dto.RewardItemDto;
import com.grepp.spring.app.model.reward.entity.OwnItem;
import com.grepp.spring.app.model.reward.entity.RewardItem;
import com.grepp.spring.app.model.reward.repository.OwnItemRepository;
import com.grepp.spring.app.model.reward.repository.RewardItemRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnItemService {

    private final OwnItemRepository ownItemRepository;
    private final RewardItemRepository rewardItemRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public void purchaseItem(Long memberId, Long itemId){

        // 1. 아이템 조회
        RewardItem rewardItem = rewardItemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("RewardItem not found"));


        // 2. 회원 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member not found"));

        // 3. 포인트 확인
        int itemPrice = rewardItem.getPrice();
        int memberPoint = member.getRewardPoints();

        if (memberPoint < itemPrice) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        // 4. 포인트 차감
        member.deductRewardPoints(itemPrice);


        OwnItem ownItem = OwnItem.builder().memberId(memberId).isUsed(false).activated(true).rewardItem(rewardItem).build();
        ownItemRepository.save(ownItem);




    }

    public void getOwnItems(Long memberId, Long itemId) {
        List<OwnItem> items = ownItemRepository.findAll();

        return items.stream()
            .map(RewardItemDto::fromEntity)
            .collect(Collectors.toList());



    }

}
