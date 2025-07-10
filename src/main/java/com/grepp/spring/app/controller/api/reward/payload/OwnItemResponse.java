package com.grepp.spring.app.controller.api.reward.payload;


import com.grepp.spring.app.model.reward.dto.OwnItemDto;
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
     private Long item_id;
     private Long own_item_id;
     private String name;
     private String type;  // ItemType을 문자열로 변환해서 담을 예정
     private boolean is_used;

    public static OwnItemResponse from(OwnItemDto dto) {
        return OwnItemResponse.builder()
            .item_id(dto.getItemId())
            .own_item_id(dto.getOwnItemId())
            .name(dto.getName())
            .type(dto.getItemtype().name())  // enum to string
            .is_used(dto.isUsed())
            .build();
    }

}
