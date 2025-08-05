package com.grepp.spring.app.model.reward.dto.internal;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class ItemSetDto
 {

    private Long hat;
    private Long hair;
    private Long face;
    private Long top;
     @Override
     public String toString() {
         return "hat:" + hat + ",hair:" + hair + ",face:" + face + ",top:" + top;
     }

}
