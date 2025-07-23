package com.grepp.spring.app.model.reward.repository;


import com.grepp.spring.app.model.reward.entity.OwnItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnItemIdGetRepository extends JpaRepository<OwnItem,Long>{

    @Query("select o.rewardItem.itemId "
        + "from OwnItem o where o.isUsed = true "
        + "and o.rewardItem.itemType != 'BACKGROUND' and o.rewardItem.itemType != 'THEME' and o.memberId = :memberId")
    List<Long> findOwnItemIdsByMemberId(@Param("memberId") Long memberId);

}
