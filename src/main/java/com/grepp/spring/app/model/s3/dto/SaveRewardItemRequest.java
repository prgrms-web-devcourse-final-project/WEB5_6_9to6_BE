package com.grepp.spring.app.model.s3.dto;

import lombok.Data;

@Data
public class SaveRewardItemRequest {
    private String name;
    private int price;
    private String type;
}

