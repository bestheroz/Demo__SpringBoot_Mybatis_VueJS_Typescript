package com.github.bestheroz.sample.api.entity.member;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableMemberRepository extends CrudRepository<TableMemberVO, String> {
    Optional<TableMemberVO> findByToken(String token);
}
