package com.grepp.spring.app.model.reward.service;


import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import com.grepp.spring.app.model.reward.code.ItemType;
import com.grepp.spring.app.model.reward.dto.internal.ItemSetDto;
import com.grepp.spring.app.model.reward.dto.internal.OwnItemDto;
import com.grepp.spring.app.model.reward.entity.OwnItem;
import com.grepp.spring.app.model.reward.entity.RewardItem;
import com.grepp.spring.app.model.reward.repository.OwnItemRepository;
import com.grepp.spring.app.model.reward.repository.RewardItemRepository;
import com.grepp.spring.infra.error.exceptions.AlreadyExistException;
import com.grepp.spring.infra.error.exceptions.InsufficientRewardPointsException;
import com.grepp.spring.infra.response.ResponseCode;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OwnItemService {

    private final OwnItemRepository ownItemRepository;
    private final RewardItemRepository rewardItemRepository;
    private final MemberRepository memberRepository;
    private final EntityManager entityManager;


    @Transactional(readOnly = true)
    public ItemSetDto getUseItemList(Long memberId) {

        //1. 멤버가 들고 있는 아이템만 뽑아서 들고오기
        List<OwnItem> ownItems = ownItemRepository.findByMemberIdAndIsUsedTrue(memberId);
        //2. 들고온 아이템 itemset 형태로 변환
        return  convertToItemSet(ownItems);



    }
    public ItemSetDto convertToItemSet(List<OwnItem> ownItems) {
        Long hat = null, hair = null, face = null, top = null;

        for (OwnItem ownItem : ownItems) {
            RewardItem rewardItem = ownItem.getRewardItem();
            if (rewardItem == null || rewardItem.getItemType() == null) continue;

            Long rewardItemId = rewardItem.getItemId();

            switch (rewardItem.getItemType()) {
                case HAT -> hat = rewardItemId;
                case HAIR -> hair = rewardItemId;
                case FACE -> face = rewardItemId;
                case TOP -> top = rewardItemId;
            }
        }

        return ItemSetDto.builder()
            .hat(hat)
            .hair(hair)
            .face(face)
            .top(top)// 이미지 생성 로직 필요 시 따로 처리
            .build();
    }





    @Transactional
    public void purchaseItem(Long memberId, Long itemId){

        // 1. 아이템 조회
        RewardItem rewardItem = rewardItemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("RewardItem not found"));


        // 2. 회원 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("Member not found"));

        // 3. 아이템 소유 여부 확인
        if(ownItemRepository.existsByMemberIdAndRewardItem_ItemId(memberId, itemId))
        {
            throw new AlreadyExistException(ResponseCode.ALREADY_EXIST);
        }



        // 4. 포인트 확인
        int itemPrice = rewardItem.getPrice();
        int memberPoint = member.getRewardPoints();

        if (memberPoint < itemPrice) {
            throw new InsufficientRewardPointsException();
        }

        // 5. 포인트 차감
        member.deductRewardPoints(itemPrice);


        OwnItem ownItem = OwnItem.builder().memberId(memberId).isUsed(false).activated(true).rewardItem(rewardItem).build();
        ownItemRepository.save(ownItem);


    }

    @Transactional(readOnly = true)
    public List<OwnItemDto> getOwnItems(Long memberId) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("Member not found"));

        return ownItemRepository.findOwnItemsByMemberId(memberId);


    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ItemType changeOwnItems(Long memberId, long ownItemId) {
        OwnItem currentItem = ownItemRepository.findById(ownItemId)
            .orElseThrow(()-> new NotFoundException("OwnItem not found" + ownItemId));


        // 현재 아이템의 타입 판별
            ItemType itemType = currentItem.getRewardItem().getItemType();
        ownItemRepository.bulkUnsetUsedItemsByType(memberId, itemType);

        entityManager.flush();
        entityManager.clear();

        OwnItem freshItem = ownItemRepository.findById(ownItemId).orElseThrow();
        freshItem.use(true);


return itemType;
    }
}
