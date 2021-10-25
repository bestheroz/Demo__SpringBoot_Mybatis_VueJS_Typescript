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
          "UPDATE ADMIN SET LOGIN_FAIL_CNT = LOGIN_FAIL_CNT + 1 WHERE ID = #{id, jdbcType=BIGINT}")
  void plusLoginFailCnt(String id);

  @Update(value = "UPDATE ADMIN SET PASSWORD = NULL WHERE ID = #{id, jdbcType=BIGINT}")
  void resetPassword(String id);

  @Select(value = "SELECT ID AS VALUE, NAME AS TEXT FROM ADMIN")
  List<CodeVO<Long>> getCodes();
}
