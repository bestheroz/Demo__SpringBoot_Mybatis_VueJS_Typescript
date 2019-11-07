package com.github.bestheroz.sample.api.auth;

import com.github.bestheroz.standard.common.exception.CommonException;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AuthDAO {
    @Update("UPDATE SAMPLE_MEMBER_MST SET LOGIN_FAIL_CNT = LOGIN_FAIL_CNT + 1 WHERE MEMBER_ID = #{memberId, jdbcType=VARCHAR}")
    void updatePlusLoginFailCnt(final String memberId) throws CommonException;

    @Update("UPDATE SAMPLE_MEMBER_MST SET LOGIN_FAIL_CNT = 0 WHERE MEMBER_ID = #{memberId, jdbcType=VARCHAR}")
    void updateZeroLoginFailCnt(final String memberId) throws CommonException;
}
