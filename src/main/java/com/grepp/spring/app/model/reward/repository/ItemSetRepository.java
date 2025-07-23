package com.grepp.spring.app.model.reward.repository;

import com.grepp.spring.app.model.reward.entity.ItemSet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemSetRepository extends JpaRepository<ItemSet,Long> {

    Optional<ItemSet> findByHatAndHairAndFaceAndTop(
        Long hat, Long hair, Long face, Long top
    );




}
