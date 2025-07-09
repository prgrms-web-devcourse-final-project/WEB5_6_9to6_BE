package com.grepp.spring.app.model.reward.service;

import com.grepp.spring.app.model.reward.dto.RewardItemDto;
import com.grepp.spring.app.model.reward.entity.RewardItem;
import com.grepp.spring.app.model.reward.repository.RewardItemRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RewardItemService {

    private final RewardItemRepository rewardItemRepository;

    public List<RewardItemDto> getItemList(){
        List<RewardItem> items = rewardItemRepository.findAll();

        return items.stream()
            .filter(RewardItem::isActivated)
            .map(RewardItemDto::fromEntity)
            .collect(Collectors.toList());

    }

}
