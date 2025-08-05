package com.grepp.spring.app.model.chat.dto.response;

public record SessionUserInfo (
    Long memberId,
    Long studyId,
    String email,
    String nickname,
    String image

){

}
