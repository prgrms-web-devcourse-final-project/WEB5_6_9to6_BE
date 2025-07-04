package com.grepp.studium.own_item.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OwnItemDTO {

    private Integer ownItemId;

    @NotNull
    @JsonProperty("isUsed")
    private Boolean isUsed;

    @NotNull
    private Boolean activated;

    @NotNull
    private Integer member;

    @NotNull
    private Integer item;

}
