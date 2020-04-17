package com.github.bestheroz.sample.api.entity.member;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableMemberRepository extends CrudRepository<TableMemberVO, String> {
    Optional<TableMemberVO> findByToken(String token);

    @Query(value = "UPDATE MEMBER SET LOGIN_FAIL_CNT = LOGIN_FAIL_CNT + 1 WHERE ID = :id", nativeQuery = true)
    @Modifying
    void plusLoginFailCnt(@Param("id") String id);
}
