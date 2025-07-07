package com.grepp.studium.reward_item.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RewardItemDTO {

    private Integer itemId;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private Integer price;

    @NotNull
    @Size(max = 255)
    private String type;

    @NotNull
    private Boolean activated;

}
