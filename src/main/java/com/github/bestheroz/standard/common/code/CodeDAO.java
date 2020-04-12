package com.github.bestheroz.standard.common.code;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CodeDAO {
    @Select(value = "SELECT SCD.CODE AS VALUE, SCD.NAME AS TEXT FROM SAMPLE_CODE_DET SCD WHERE SCD.IS_USING = 'Y' AND SCD.GROUP_CODE = #{groupCode, jdbcType=VARCHAR} ORDER BY SCD.DISPLAY_ORDER ASC")
    List<CodeVO> getCodeVOList(final String groupCode);
}
