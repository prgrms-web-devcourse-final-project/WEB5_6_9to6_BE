package com.grepp.spring.app.model.reward.repository;

import com.grepp.spring.app.model.reward.entity.ItemSet;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemSetRepository extends JpaRepository<ItemSet,Long> {

    Optional<ItemSet> findByHatAndHairAndFaceAndTop(
        Long hat, Long hair, Long face, Long top
    );

    @Query("SELECT i.image FROM ItemSet i WHERE i.setId = :itemId")
    String findImageByItemId(@Param("itemId") Long itemId);


}
