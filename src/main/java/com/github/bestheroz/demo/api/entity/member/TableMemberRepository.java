package com.github.bestheroz.demo.api.entity.member;

import com.github.bestheroz.standard.common.code.CodeVO;
import com.github.bestheroz.standard.common.mybatis.SqlRepository;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TableMemberRepository extends SqlRepository<TableMemberEntity> {
  @Update(
      value =
          "UPDATE MEMBER SET LOGIN_FAIL_CNT = LOGIN_FAIL_CNT + 1 WHERE ID = #{id, jdbcType=VARCHAR}")
  void plusLoginFailCnt(String id);

  @Update(value = "UPDATE MEMBER SET PASSWORD = NULL WHERE ID = #{id, jdbcType=VARCHAR}")
  void resetPassword(String id);

  @Select(value = "SELECT ID AS VALUE, NAME AS TEXT FROM MEMBER")
  List<CodeVO> getCodes();
}
