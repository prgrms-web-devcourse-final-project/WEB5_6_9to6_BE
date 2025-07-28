package com.grepp.spring.app.controller.api.reward.payload;

import com.grepp.spring.app.model.reward.code.ItemType;
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClothesDto {


        private String name;
        @NotNull
        private ItemType category;
        @NotNull
        private List<Long> itemId;
    }

}
