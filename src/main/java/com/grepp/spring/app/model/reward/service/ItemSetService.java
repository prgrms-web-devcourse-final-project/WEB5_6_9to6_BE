package com.grepp.spring.app.model.reward.service;

import com.grepp.spring.app.model.reward.dto.response.ImageResponse;
import com.grepp.spring.app.model.reward.dto.request.SaveImageRequest;
import com.grepp.spring.app.model.reward.code.ItemType;
import com.grepp.spring.app.model.reward.dto.internal.ItemSetDto;
import com.grepp.spring.app.model.reward.entity.ItemSet;
import com.grepp.spring.app.model.reward.entity.RewardItem;
import com.grepp.spring.app.model.reward.repository.ItemSetRepository;
import com.grepp.spring.app.model.reward.repository.RewardItemRepository;
import com.grepp.spring.app.model.s3.service.S3Service;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ItemSetService {

    private final ItemSetRepository itemSetRepository;
    private final RewardItemRepository rewardItemRepository;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    @Cacheable(value = "itemSetImageCache", key = "#itemSetDto.toString()")
    public Optional<ImageResponse> ExistItemSet(ItemSetDto itemSetDto) {

        return itemSetRepository.findByHatAndHairAndFaceAndTop(
            itemSetDto.getHat(), itemSetDto.getHair(), itemSetDto.getFace(), itemSetDto.getTop()
        ).map(ItemSet->new ImageResponse(ItemSet.getImage()));
    }

    @Transactional
    @CacheEvict(value = "itemSetImageCache",
        key = "'hat:' + #dto.clothes.?[category == 'hat'].[0].itemId[0] + ',' + " +
            "'hair:' + #dto.clothes.?[category == 'hair'].[0].itemId[0] + ',' + " +
            "'face:' + #dto.clothes.?[category == 'face'].[0].itemId[0] + ',' + " +
            "'top:' + #dto.clothes.?[category == 'top'].[0].itemId[0]")
    public void saveImage(SaveImageRequest dto, MultipartFile imageFile) {


        // 1. category별 itemId 추출
        Map<ItemType, Long> categoryToItemId = dto.getClothes().stream()
            .filter(clothesDto -> clothesDto.getItemId() != null && !clothesDto.getItemId().isEmpty())
            .collect(Collectors.toMap(
                SaveImageRequest.ClothesDto::getCategory,
                clothesDto -> clothesDto.getItemId().get(0)
            ));

        // 2. 존재하는 아이템 ID 검증
        List<Long> allItemIds = new ArrayList<>(categoryToItemId.values());

        Set<Long> existingIdSet = rewardItemRepository.findAllByItemIdIn(allItemIds)
            .stream()
            .map(RewardItem::getItemId)
            .collect(Collectors.toSet());

        List<Long> notFoundIds = allItemIds.stream()
            .filter(id -> !existingIdSet.contains(id))
            .toList();

        if (!notFoundIds.isEmpty()) {
            throw new NotFoundException("존재하지 않는 아이템 ID: " + notFoundIds);
        }

        // 3. S3에 이미지 업로드
        String imageUrl;
        try {
            imageUrl = s3Service.uploadFile(imageFile, "item-sets");
        } catch (IOException e) {
            throw new RuntimeException("S3 이미지 업로드 실패", e);
        }

        // 4. ItemSet 저장
        ItemSet itemSet = ItemSet.builder()
            .hat(categoryToItemId.get(ItemType.HAT))
            .hair(categoryToItemId.get(ItemType.HAIR))
            .face(categoryToItemId.get(ItemType.FACE))
            .top(categoryToItemId.get(ItemType.TOP))
            .image(imageUrl)
            .build();

        itemSetRepository.save(itemSet);
    }
}
