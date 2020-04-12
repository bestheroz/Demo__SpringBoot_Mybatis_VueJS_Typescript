package com.github.bestheroz.sample.api.entity.samplemembermst;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableSampleMemberMstRepository extends JpaRepository<TableSampleMemberMstVO, String> {
    Optional<TableSampleMemberMstVO> findByToken(String token);
}
