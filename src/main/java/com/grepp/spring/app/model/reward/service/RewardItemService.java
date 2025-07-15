package com.grepp.spring.app.model.reward.service;

import com.grepp.spring.app.model.reward.dto.RewardItemDto;
import com.grepp.spring.app.model.reward.entity.RewardItem;
import com.grepp.spring.app.model.reward.repository.RewardItemRepository;
import com.grepp.spring.infra.error.exceptions.OutOfMinimumPageException;
import com.grepp.spring.infra.error.exceptions.OutOfMinimumPageSizeException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<RewardItemDto> getItemList(Pageable pageable){
        if (pageable.getPageNumber() < 1) throw new OutOfMinimumPageException("Page는 1보다 작을 수 없습니다.");
        if (pageable.getPageSize() < 1)throw new OutOfMinimumPageSizeException("Size는 1보다 작을 수 없습니다.");

        Pageable modifiedPageable = PageRequest.of(
            pageable.getPageNumber() - 1,
            pageable.getPageSize(),
            pageable.getSort()
        );

        Page<RewardItem> itemsPage = rewardItemRepository.findAllByActivatedIsTrue(modifiedPageable);
        return itemsPage.map(RewardItemDto::fromEntity);
    }

}
