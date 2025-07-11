package com.grepp.spring.app.model.reward.service;

import com.grepp.spring.app.controller.api.reward.payload.ImageResponse;
import com.grepp.spring.app.controller.api.reward.payload.SaveImageRequestDto;
import com.grepp.spring.app.model.reward.dto.ItemSetDto;
import com.grepp.spring.app.model.reward.entity.ItemSet;
import com.grepp.spring.app.model.reward.entity.RewardItem;
import com.grepp.spring.app.model.reward.repository.ItemSetRepository;
import com.grepp.spring.app.model.reward.repository.RewardItemRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemSetService {

    private final ItemSetRepository itemSetRepository;
    private final RewardItemRepository rewardItemRepository;

    public Optional<ImageResponse> ExistItemSet(ItemSetDto itemSetDto) {

        return itemSetRepository.findByHatAndHairAndFaceAndTopAndBottom(
            itemSetDto.getHat(), itemSetDto.getHair(), itemSetDto.getFace(), itemSetDto.getTop(), itemSetDto.getBottom()
        ).map(ItemSet->new ImageResponse(ItemSet.getImage()));
    }

    @Transactional
    public void saveImage(SaveImageRequestDto dto) {

        if (dto.getWholeImageUrl() == null) {
            throw new IllegalArgumentException("이미지가 없습니다.");
        }

        // 1. category별 itemId 추출
        Map<String, Long> categoryToItemId = dto.getClothes().stream()
            .filter(clothesDto -> clothesDto.getItemIds() != null && !clothesDto.getItemIds().isEmpty())
            .collect(Collectors.toMap(
                SaveImageRequestDto.ClothesDto::getCategory,
                clothesDto -> clothesDto.getItemIds().get(0)
            ));

        List<Long> allItemIds = new ArrayList<>(categoryToItemId.values());

        // 2. 존재하는 아이템 ID만 필터링
        List<Long> existingIds = rewardItemRepository.findAllByItemIdIn(allItemIds)
            .stream()
            .map(RewardItem::getItemId)
            .toList();

        List<Long> notFoundIds = allItemIds.stream()
            .filter(id -> !existingIds.contains(id))
            .toList();

        if (!notFoundIds.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 아이템 ID: " + notFoundIds);
        }
        // 3. ItemSet 저장
        ItemSet itemSet = ItemSet.builder()
            .hat(categoryToItemId.get("hat"))
            .hair(categoryToItemId.get("hair"))
            .face(categoryToItemId.get("face"))
            .top(categoryToItemId.get("top"))
            .bottom(categoryToItemId.get("bottom"))
            .image(dto.getWholeImageUrl())
            .build();

        itemSetRepository.save(itemSet);

    }




}
