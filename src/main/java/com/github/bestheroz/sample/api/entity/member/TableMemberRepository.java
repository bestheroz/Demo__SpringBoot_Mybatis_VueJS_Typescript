package com.github.bestheroz.sample.api.entity.member;

import com.github.bestheroz.standard.common.mybatis.SqlRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TableMemberRepository extends SqlRepository<TableMemberEntity> {

    @Update(value = "UPDATE MEMBER SET LOGIN_FAIL_CNT = LOGIN_FAIL_CNT + 1 WHERE ID = #{id, jdbcType=VARCHAR}")
    void plusLoginFailCnt(String id);

    @Update(value = "UPDATE MEMBER SET PASSWORD = NULL WHERE ID = #{id, jdbcType=VARCHAR}")
    void resetPassword(String id);
}
