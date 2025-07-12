package com.grepp.spring.infra.error.exceptions;

public class InsufficientRewardPointsException extends RewardApiException {

    public InsufficientRewardPointsException() {
        super("포인트가 부족합니다.");
    }
}
