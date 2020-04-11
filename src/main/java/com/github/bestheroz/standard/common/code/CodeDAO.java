package com.github.bestheroz.standard.common.code;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CodeDAO {
    @Select(value = "SELECT COD2T.CODE AS VALUE, COD2T.CODENM AS TEXT FROM MGMT_SYSCOD2T COD2T WHERE COD2T.USE_YN = 'Y' AND COD2T.GRCODE = #{groupCode, jdbcType=VARCHAR} ORDER BY COD2T.SORTSEQ ASC")
    List<CodeVO> getCodeVOList(final String groupCode);
}
