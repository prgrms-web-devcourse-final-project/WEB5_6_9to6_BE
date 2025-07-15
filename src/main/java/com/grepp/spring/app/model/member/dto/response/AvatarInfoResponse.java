package com.grepp.spring.app.model.member.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AvatarInfoResponse {

    private String avatarImage;
    List<Long> ItemIds;

    @Builder
    public AvatarInfoResponse(String avatarImage, List<Long> itemIds) {
        this.avatarImage = avatarImage;
        ItemIds = itemIds;
    }
}
