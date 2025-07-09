package com.grepp.spring.app.model.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class EmailDuplicatedCheckResponse {
    private boolean duplicated;

}
