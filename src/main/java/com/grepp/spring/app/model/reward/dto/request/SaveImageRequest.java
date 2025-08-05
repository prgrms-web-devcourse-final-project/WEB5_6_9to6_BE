package com.grepp.spring.app.model.reward.dto.request;

import com.grepp.spring.app.model.reward.code.ItemType;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SaveImageRequest {
    private List<ClothesDto> clothes;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static class ClothesDto {


        private String name;
        @NotNull
        private ItemType category;
        @NotNull
        private List<Long> itemId;
    }

}
