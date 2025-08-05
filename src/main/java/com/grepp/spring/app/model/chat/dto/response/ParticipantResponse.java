package com.grepp.spring.app.model.chat.dto.response;

public record ParticipantResponse (
    Long memberId,
    String nickname,
    String image,
    String status
){

}
