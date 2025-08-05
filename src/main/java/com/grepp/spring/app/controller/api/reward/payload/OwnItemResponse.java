package com.grepp.spring.app.controller.api.reward.payload;


import com.grepp.spring.app.model.reward.dto.internal.OwnItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnItemResponse

 {
     private Long itemId;
     private Long ownItemId;
     private String name;
     private String type;  // ItemType을 문자열로 변환해서 담을 예정
     private boolean isUsed;

    public static OwnItemResponse from(OwnItemDto dto) {
        return OwnItemResponse.builder()
            .itemId(dto.getItemId())
            .ownItemId(dto.getOwnItemId())
            .name(dto.getName())
            .type(dto.getItemtype().name())  // enum to string
            .isUsed(dto.isUsed())
            .build();
    }

}
