package com.grepp.spring.app.model.reward.service;

import com.grepp.spring.app.controller.api.reward.payload.ImageResponse;
import com.grepp.spring.app.controller.api.reward.payload.SaveImageRequestDto;
import com.grepp.spring.app.model.reward.dto.ItemSetDto;
import com.grepp.spring.app.model.reward.entity.ItemSet;
import com.grepp.spring.app.model.reward.repository.ItemSetRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemSetService {

    private final ItemSetRepository itemSetRepository;

    public Optional<ImageResponse> ExistItemSet(ItemSetDto itemSetDto) {

        return itemSetRepository.findByHatAndHairAndFaceAndTopAndBottom(
            itemSetDto.getHat(), itemSetDto.getHair(), itemSetDto.getFace(), itemSetDto.getTop(), itemSetDto.getBottom()
        ).map(ItemSet->new ImageResponse(ItemSet.getImage()));
    }

    @Transactional
    public void saveImage(SaveImageRequestDto dto) {
        ItemSet itemset = ItemSet.builder()
            .hat(dto.getHat())
            .hair(dto.getHair())
            .face(dto.getFace())
            .top(dto.getTop())
            .bottom(dto.getBottom())
            .image(dto.getImage())
            .build();

        itemSetRepository.save(itemset);

    }




}
