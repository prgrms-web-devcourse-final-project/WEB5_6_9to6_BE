package com.grepp.spring.app.model.reward.service;

import com.grepp.spring.app.controller.api.reward.payload.ImageResponse;
import com.grepp.spring.app.controller.api.reward.payload.SaveImageRequest;
import com.grepp.spring.app.model.reward.dto.ItemSetDto;
import com.grepp.spring.app.model.reward.entity.ItemSet;
import com.grepp.spring.app.model.reward.entity.RewardItem;
import com.grepp.spring.app.model.reward.repository.ItemSetRepository;
import com.grepp.spring.app.model.reward.repository.RewardItemRepository;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemSetService {

    private final ItemSetRepository itemSetRepository;
    private final RewardItemRepository rewardItemRepository;

    @Transactional(readOnly = true)
    public Optional<ImageResponse> ExistItemSet(ItemSetDto itemSetDto) {

        return itemSetRepository.findByHatAndHairAndFaceAndTop(
            itemSetDto.getHat(), itemSetDto.getHair(), itemSetDto.getFace(), itemSetDto.getTop()
        ).map(ItemSet->new ImageResponse(ItemSet.getImage()));
    }

    @Transactional
    public void saveImage(SaveImageRequest dto) {

        // 1. category별 itemId 추출
        Map<String, Long> categoryToItemId = dto.getClothes().stream()
            .filter(clothesDto -> clothesDto.getItemId() != null && !clothesDto.getItemId().isEmpty())
            .collect(Collectors.toMap(
                SaveImageRequest.ClothesDto::getCategory,
                clothesDto -> clothesDto.getItemId().get(0)
            ));

        List<Long> allItemIds = new ArrayList<>(categoryToItemId.values());

        // 2. 존재하는 아이템 ID만 필터링
        List<Long> existingIds = rewardItemRepository.findAllByItemIdIn(allItemIds)
            .stream()
            .map(RewardItem::getItemId)
            .toList();

        Set<Long> existingIdSet = new HashSet<>(existingIds);

        List<Long> notFoundIds = allItemIds.stream()
            .filter(id -> !existingIdSet.contains(id))
            .toList();

        if (!notFoundIds.isEmpty()) {
            throw new NotFoundException("존재하지 않는 아이템 ID: " + notFoundIds);
        }
        // 3. ItemSet 저장
        ItemSet itemSet = ItemSet.builder()
            .hat(categoryToItemId.get("hat"))
            .hair(categoryToItemId.get("hair"))
            .face(categoryToItemId.get("face"))
            .top(categoryToItemId.get("top"))
            .image(dto.getWholeImageUrl())
            .build();

        itemSetRepository.save(itemSet);

    }




}
