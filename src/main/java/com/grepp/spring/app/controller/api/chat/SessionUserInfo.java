package com.grepp.spring.app.controller.api.chat;

public record SessionUserInfo (
    Long memberId,
    Long studyId,
    String email,
    String nickname,
    String image

){

}
