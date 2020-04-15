package com.github.bestheroz.sample.api.entity.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableMemberRepository extends JpaRepository<TableMemberVO, String> {
    Optional<TableMemberVO> findByToken(String token);
}
