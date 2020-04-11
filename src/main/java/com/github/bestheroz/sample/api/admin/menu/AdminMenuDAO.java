package com.github.bestheroz.sample.api.admin.menu;

import com.github.bestheroz.standard.common.code.CodeVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AdminMenuDAO {
    @Select("SELECT SMM.MENU_ID, SMM.MENU_NAME FROM SAMPLE_MENU_MST SMM WHERE SMM.MENU_TYPE = 'G' ORDER BY SMM.MENU_ID ASC")
    @Results(value = {@Result(column = "MENU_ID", property = "value"), @Result(column = "MENU_NAME", property = "text")})
    List<CodeVO> getMenuTypeG() throws BusinessException;
}
