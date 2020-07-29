package com.github.bestheroz.sample.api.entity.member;

import com.github.bestheroz.standard.common.repository.SqlRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface TableMemberRepository extends SqlRepository<TableMemberEntity, String> {
    Optional<TableMemberEntity> findByToken(String token);

    @Select(value = "UPDATE MEMBER SET LOGIN_FAIL_CNT = LOGIN_FAIL_CNT + 1 WHERE ID = #{id, jdbcType=VARCHAR}")
    void plusLoginFailCnt(String id);
}
