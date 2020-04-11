package com.github.bestheroz.sample.api.entity.samplemembermst;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableSampleMemberMstRepository extends JpaRepository<TableSampleMemberMstVO, String> {
    Optional<TableSampleMemberMstVO> findByToken(String token);

    @Query(value = "UPDATE SAMPLE_MEMBER_MST SET LOGIN_FAIL_CNT = 0 WHERE MEMBER_ID = ?1", nativeQuery = true)
    void updateLoginFailCntToZero(String memberId);

    @Query(value = "UPDATE SAMPLE_MEMBER_MST SET TOKEN = ?2 WHERE MEMBER_ID = ?1", nativeQuery = true)
    void updateToken(String memberId, String token);
}
