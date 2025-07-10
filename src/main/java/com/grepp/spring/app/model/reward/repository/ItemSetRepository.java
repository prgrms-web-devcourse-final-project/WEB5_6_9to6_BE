package com.grepp.spring.app.model.reward.repository;

import com.grepp.spring.app.model.reward.entity.ItemSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemSetRepository extends JpaRepository<ItemSet,Long> {



}
