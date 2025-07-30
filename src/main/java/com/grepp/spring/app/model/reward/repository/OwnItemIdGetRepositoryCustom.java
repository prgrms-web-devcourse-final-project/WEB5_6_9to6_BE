package com.grepp.spring.app.model.reward.repository;

import java.util.List;

public interface OwnItemIdGetRepositoryCustom {

    List<Long> findIdsByMemberId(Long memberId);

}
