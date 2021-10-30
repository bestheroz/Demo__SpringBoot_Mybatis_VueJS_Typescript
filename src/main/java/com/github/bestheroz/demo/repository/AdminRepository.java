package com.github.bestheroz.demo.repository;

import com.github.bestheroz.demo.api.code.CodeVO;
import com.github.bestheroz.demo.entity.Admin;
import com.github.bestheroz.standard.common.mybatis.SqlRepository;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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

  @Select(
      value =
          """
            select id as value
                 , name as text
              from admin
            """)
  List<CodeVO<Long>> getCodes();
}
