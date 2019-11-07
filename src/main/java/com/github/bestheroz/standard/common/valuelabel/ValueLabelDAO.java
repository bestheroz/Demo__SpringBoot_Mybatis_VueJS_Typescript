package com.github.bestheroz.standard.common.valuelabel;

import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.valuelabel.response.GetValueLabeVOListResponseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ValueLabelDAO {
    @Select(value = "SELECT SCD.CODE, SCD.CODE_NAME FROM SAMPLE_CODE_DET SCD WHERE SCD.GROUP_CODE = #{grcode, jdbcType=VARCHAR} ORDER BY SCD.DISPLAY_ORDER ASC")
    @Results(value = {@Result(column = "CODE", property = "value"), @Result(column = "CODE_NAME", property = "label")})
    List<GetValueLabeVOListResponseVO> getList(final String grcode) throws CommonException;

    @Select(value = "SELECT SCD.CODE_NAME FROM SAMPLE_CODE_DET SCD WHERE SCD.GROUP_CODE = #{grcode, jdbcType=VARCHAR} AND SCD.CODE = #{code, jdbcType=VARCHAR}  ORDER BY SCD.DISPLAY_ORDER ASC")
    @Results(value = {@Result(column = "CODE_NAME", property = "label")})
    String getLabelByValue(final Map<String, Object> params) throws CommonException;
}
