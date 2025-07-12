package com.grepp.spring.app.controller.api.reward.payload;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveImageRequest {
    private List<ClothesDto> clothes;
    @NotNull
    private String wholeImageUrl;
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClothesDto {


        private String name;
        @NotNull
        private String category;
        @NotNull
        private List<Long> itemIds;
    }

}
