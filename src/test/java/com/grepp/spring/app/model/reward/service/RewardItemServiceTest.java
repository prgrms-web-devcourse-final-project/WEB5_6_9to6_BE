package com.grepp.spring.app.model.reward.service;

import com.grepp.spring.app.model.reward.code.ItemType;
import com.grepp.spring.app.model.reward.dto.internal.RewardItemDto;
import com.grepp.spring.app.model.reward.entity.RewardItem;
import com.grepp.spring.app.model.reward.repository.RewardItemRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class RewardItemServiceTest {

    @Mock
    RewardItemRepository rewardItemRepository;
    @InjectMocks
    RewardItemService rewardItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetItemList() {
        RewardItem rewardItem = new RewardItem(
            1L,                        // itemId
            "name",                   // name
            0,                        // price
            ItemType.BACKGROUND,      // itemType
            true,                     // activated
            "https://example.com/img" // image
        );
        when(rewardItemRepository.findAll()).thenReturn(List.of(rewardItem));

        List<RewardItemDto> result = rewardItemService.getItemList();
        Assertions.assertEquals(
            List.of(new RewardItemDto(Long.valueOf(1L), "name", 0, ItemType.BACKGROUND)), result);
    }

}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme