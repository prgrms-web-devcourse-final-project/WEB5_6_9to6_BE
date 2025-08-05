package com.grepp.spring.app.controller.api;

import com.grepp.spring.app.model.reward.dto.response.RewardItemResponse;
import com.grepp.spring.app.model.auth.domain.Principal;
import com.grepp.spring.app.model.member.service.MemberService;
import com.grepp.spring.app.model.reward.code.ItemType;
import com.grepp.spring.app.model.reward.dto.internal.RewardItemDto;
import com.grepp.spring.app.model.reward.service.ItemSetService;
import com.grepp.spring.app.model.reward.service.OwnItemService;
import com.grepp.spring.app.model.reward.service.RewardItemService;
import com.grepp.spring.infra.response.CommonResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.Mockito.*;

class RewardControllerTest {

    @Mock
    RewardItemService rewardItemService;
    @Mock
    OwnItemService ownItemService;
    @Mock
    ItemSetService itemSetService;
    @Mock
    MemberService memberService;
    @InjectMocks
    RewardController rewardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUpAuthentication() {
        UserDetails mockUser = new org.springframework.security.core.userdetails.User(
            "user@example.com", "password", Collections.emptyList()
        );
        Authentication auth = new UsernamePasswordAuthenticationToken(mockUser, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }


    // 정상적인 값을 반환할때
    @Test
    void testGetRewardItemsAtNormal() {
        when(rewardItemService.getItemList()).thenReturn(
            List.of(new RewardItemDto(Long.valueOf(1), "name", 0, ItemType.BACKGROUND)));

        ResponseEntity<CommonResponse<RewardItemResponse>> result = rewardController.getRewardItems();
        Assertions.assertEquals(new ResponseEntity<CommonResponse<RewardItemResponse>>(
            new CommonResponse<RewardItemResponse>("0000", "정상적으로 완료되었습니다.", new RewardItemResponse(
                List.of(new RewardItemDto(Long.valueOf(1), "name", 0, ItemType.BACKGROUND)))), null,
            200), result);
    }


    // 빈 리스트 반환할때
    @Test
    void testGetRewardItemsIfEmpty() {
        when(rewardItemService.getItemList()).thenReturn(Collections.emptyList());

        ResponseEntity<CommonResponse<RewardItemResponse>> result = rewardController.getRewardItems();
        Assertions.assertEquals(new ResponseEntity<CommonResponse<RewardItemResponse>>(
            new CommonResponse<RewardItemResponse>("0000", "정상적으로 완료되었습니다.", new RewardItemResponse(Collections.emptyList())),
            null,
            HttpStatus.OK
        ), result);
    }



    @Test
    void testPurchaseItem() {
        Principal mockUser = new Principal(1L,"user@example.com", "password", Collections.emptyList());
        Authentication auth = new UsernamePasswordAuthenticationToken(mockUser, null);

        ResponseEntity<CommonResponse<Map<String, Object>>> result = rewardController.purchaseItem(0L);

        verify(ownItemService).purchaseItem(anyLong(), anyLong());

        Assertions.assertEquals(
            new ResponseEntity<>(
                new CommonResponse<>("0000", "정상적으로 완료되었습니다.", Map.of()),
                null,
                201
            ),
            result
        );
    }

}



//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme