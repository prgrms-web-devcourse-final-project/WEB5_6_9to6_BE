package com.grepp.spring.app.model.member.repository;

import com.grepp.spring.app.model.member.dto.response.RequiredMemberInfoResponse;

public interface MemberRepositoryCustom {

    Long findIdByEmail(String email);

    String getNickname(Long id);

    RequiredMemberInfoResponse getRequiredMemberInfo(Long memberId);

    String getAvatarImage(Long memberId);

    void addRewardPoints(Long memberId, int points);

}