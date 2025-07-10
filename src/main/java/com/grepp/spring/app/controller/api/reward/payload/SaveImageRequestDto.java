package com.grepp.spring.app.controller.api.reward.payload;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveImageRequestDto {
    private Long hat;
    private Long hair;
    private Long face;
    private Long top;
    private Long bottom;
    private String image;
}
