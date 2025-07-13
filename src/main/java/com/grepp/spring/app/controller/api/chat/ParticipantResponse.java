package com.grepp.spring.app.controller.api.chat;

public record ParticipantResponse (
    Long memberId,
    String nickname,
    String status
){

}
