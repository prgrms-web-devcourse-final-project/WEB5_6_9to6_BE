package com.grepp.studium.member.repos;

import com.grepp.studium.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Integer> {

    boolean existsByEmailIgnoreCase(String email);

}
