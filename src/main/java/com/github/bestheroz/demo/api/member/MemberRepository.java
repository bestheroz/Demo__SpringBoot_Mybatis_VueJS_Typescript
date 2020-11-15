package com.github.bestheroz.demo.api.member;

import com.github.bestheroz.standard.common.code.CodeVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.mybatis.SqlRepository;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MemberRepository extends SqlRepository<CodeVO> {
  @Select(
    value = "SELECT M.ID AS VALUE, M.NAME AS TEXT FROM MEMBER M ORDER BY M.NAME ASC"
  )
  List<CodeVO> getCodeItems() throws BusinessException;
}
