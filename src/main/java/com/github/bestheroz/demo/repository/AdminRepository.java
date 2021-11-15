package com.github.bestheroz.demo.repository;

import com.github.bestheroz.demo.entity.Admin;
import com.github.bestheroz.standard.common.mybatis.SqlRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AdminRepository extends SqlRepository<Admin> {
  @Update(
      value =
          """
        update admin
           set sign_in_fail_cnt = sign_in_fail_cnt + 1
         where id = #{id, jdbcType=BIGINT}
        """)
  void plusSignInFailCnt(Long id);

  @Update(
      value =
          """
        update admin
           set sign_in_fail_cnt = 0
             , token = #{token, jdbcType=VARCHAR}
         where id = #{id, jdbcType=BIGINT}
        """)
  void updateTokenAndSignInFailCnt(Long id, String token);
}
