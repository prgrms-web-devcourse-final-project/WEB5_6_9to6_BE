package com.grepp.studium.choice.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChoiceDTO {

    private Integer choiceId;

    @NotNull
    @Size(max = 255)
    private String choice1;

    @NotNull
    @Size(max = 255)
    private String choice2;

    @NotNull
    @Size(max = 255)
    private String choice3;

    @NotNull
    @Size(max = 255)
    private String choice4;

    @NotNull
    private Boolean activated;

    @NotNull
    private Integer quiz;

}
