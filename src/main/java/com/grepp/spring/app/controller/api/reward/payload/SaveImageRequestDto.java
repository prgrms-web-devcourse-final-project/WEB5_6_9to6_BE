package com.grepp.spring.app.controller.api.reward.payload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveImageRequestDto {
    private List<ClothesDto> clothes;
    private String wholeImageUrl;
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClothesDto {
        private String name;
        private String category;
        private List<Long> itemIds;
    }

}
