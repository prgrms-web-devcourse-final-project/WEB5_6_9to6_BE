package com.grepp.spring.app.model.reward.repository;

import com.grepp.spring.app.model.reward.dto.OwnItemDto;
import com.grepp.spring.app.model.reward.entity.OwnItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnItemRepository extends JpaRepository<OwnItem,Long> {
    List<OwnItemDto> findOwnItemsByMemberId(Long memberId);

}
