package com.grepp.spring.app.model.alarm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String grantType;
    private Long expiresIn;
}
