package com.grepp.studium.reward_item.service;

import com.grepp.studium.own_item.domain.OwnItem;
import com.grepp.studium.own_item.repos.OwnItemRepository;
import com.grepp.studium.reward_item.domain.RewardItem;
import com.grepp.studium.reward_item.model.RewardItemDTO;
import com.grepp.studium.reward_item.repos.RewardItemRepository;
import com.grepp.studium.util.NotFoundException;
import com.grepp.studium.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RewardItemService {

    private final RewardItemRepository rewardItemRepository;
    private final OwnItemRepository ownItemRepository;

    public RewardItemService(final RewardItemRepository rewardItemRepository,
            final OwnItemRepository ownItemRepository) {
        this.rewardItemRepository = rewardItemRepository;
        this.ownItemRepository = ownItemRepository;
    }

    public List<RewardItemDTO> findAll() {
        final List<RewardItem> rewardItems = rewardItemRepository.findAll(Sort.by("itemId"));
        return rewardItems.stream()
                .map(rewardItem -> mapToDTO(rewardItem, new RewardItemDTO()))
                .toList();
    }

    public RewardItemDTO get(final Integer itemId) {
        return rewardItemRepository.findById(itemId)
                .map(rewardItem -> mapToDTO(rewardItem, new RewardItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final RewardItemDTO rewardItemDTO) {
        final RewardItem rewardItem = new RewardItem();
        mapToEntity(rewardItemDTO, rewardItem);
        return rewardItemRepository.save(rewardItem).getItemId();
    }

    public void update(final Integer itemId, final RewardItemDTO rewardItemDTO) {
        final RewardItem rewardItem = rewardItemRepository.findById(itemId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(rewardItemDTO, rewardItem);
        rewardItemRepository.save(rewardItem);
    }

    public void delete(final Integer itemId) {
        rewardItemRepository.deleteById(itemId);
    }

    private RewardItemDTO mapToDTO(final RewardItem rewardItem, final RewardItemDTO rewardItemDTO) {
        rewardItemDTO.setItemId(rewardItem.getItemId());
        rewardItemDTO.setName(rewardItem.getName());
        rewardItemDTO.setPrice(rewardItem.getPrice());
        rewardItemDTO.setType(rewardItem.getType());
        rewardItemDTO.setActivated(rewardItem.getActivated());
        return rewardItemDTO;
    }

    private RewardItem mapToEntity(final RewardItemDTO rewardItemDTO, final RewardItem rewardItem) {
        rewardItem.setName(rewardItemDTO.getName());
        rewardItem.setPrice(rewardItemDTO.getPrice());
        rewardItem.setType(rewardItemDTO.getType());
        rewardItem.setActivated(rewardItemDTO.getActivated());
        return rewardItem;
    }

    public ReferencedWarning getReferencedWarning(final Integer itemId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final RewardItem rewardItem = rewardItemRepository.findById(itemId)
                .orElseThrow(NotFoundException::new);
        final OwnItem itemOwnItem = ownItemRepository.findFirstByItem(rewardItem);
        if (itemOwnItem != null) {
            referencedWarning.setKey("rewardItem.ownItem.item.referenced");
            referencedWarning.addParam(itemOwnItem.getOwnItemId());
            return referencedWarning;
        }
        return null;
    }

}
