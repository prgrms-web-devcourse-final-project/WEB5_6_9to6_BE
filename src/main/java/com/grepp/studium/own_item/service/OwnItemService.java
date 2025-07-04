package com.grepp.studium.own_item.service;

import com.grepp.studium.member.domain.Member;
import com.grepp.studium.member.repos.MemberRepository;
import com.grepp.studium.own_item.domain.OwnItem;
import com.grepp.studium.own_item.model.OwnItemDTO;
import com.grepp.studium.own_item.repos.OwnItemRepository;
import com.grepp.studium.reward_item.domain.RewardItem;
import com.grepp.studium.reward_item.repos.RewardItemRepository;
import com.grepp.studium.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OwnItemService {

    private final OwnItemRepository ownItemRepository;
    private final MemberRepository memberRepository;
    private final RewardItemRepository rewardItemRepository;

    public OwnItemService(final OwnItemRepository ownItemRepository,
            final MemberRepository memberRepository,
            final RewardItemRepository rewardItemRepository) {
        this.ownItemRepository = ownItemRepository;
        this.memberRepository = memberRepository;
        this.rewardItemRepository = rewardItemRepository;
    }

    public List<OwnItemDTO> findAll() {
        final List<OwnItem> ownItems = ownItemRepository.findAll(Sort.by("ownItemId"));
        return ownItems.stream()
                .map(ownItem -> mapToDTO(ownItem, new OwnItemDTO()))
                .toList();
    }

    public OwnItemDTO get(final Integer ownItemId) {
        return ownItemRepository.findById(ownItemId)
                .map(ownItem -> mapToDTO(ownItem, new OwnItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final OwnItemDTO ownItemDTO) {
        final OwnItem ownItem = new OwnItem();
        mapToEntity(ownItemDTO, ownItem);
        return ownItemRepository.save(ownItem).getOwnItemId();
    }

    public void update(final Integer ownItemId, final OwnItemDTO ownItemDTO) {
        final OwnItem ownItem = ownItemRepository.findById(ownItemId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ownItemDTO, ownItem);
        ownItemRepository.save(ownItem);
    }

    public void delete(final Integer ownItemId) {
        ownItemRepository.deleteById(ownItemId);
    }

    private OwnItemDTO mapToDTO(final OwnItem ownItem, final OwnItemDTO ownItemDTO) {
        ownItemDTO.setOwnItemId(ownItem.getOwnItemId());
        ownItemDTO.setIsUsed(ownItem.getIsUsed());
        ownItemDTO.setActivated(ownItem.getActivated());
        ownItemDTO.setMember(ownItem.getMember() == null ? null : ownItem.getMember().getMemberId());
        ownItemDTO.setItem(ownItem.getItem() == null ? null : ownItem.getItem().getItemId());
        return ownItemDTO;
    }

    private OwnItem mapToEntity(final OwnItemDTO ownItemDTO, final OwnItem ownItem) {
        ownItem.setIsUsed(ownItemDTO.getIsUsed());
        ownItem.setActivated(ownItemDTO.getActivated());
        final Member member = ownItemDTO.getMember() == null ? null : memberRepository.findById(ownItemDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        ownItem.setMember(member);
        final RewardItem item = ownItemDTO.getItem() == null ? null : rewardItemRepository.findById(ownItemDTO.getItem())
                .orElseThrow(() -> new NotFoundException("item not found"));
        ownItem.setItem(item);
        return ownItem;
    }

}
