package com.github.bestheroz.sample.web.admin.menu;

import com.github.bestheroz.sample.web.admin.menu.response.GetSampleMenuMstVOListResponseVO;
import com.github.bestheroz.sample.web.tablevo.samplemenumst.TableSampleMenuMstVO;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.valuelabel.response.GetValueLabeVOListResponseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMenuDAO {
    List<GetSampleMenuMstVOListResponseVO> getSampleMenuMstVOList(final TableSampleMenuMstVO vo) throws CommonException;

    @Select("SELECT SMM.MENU_ID, SMM.MENU_NM FROM SAMPLE_MENU_MST SMM WHERE SMM.MENU_TYP = 'G' ORDER BY SMM.MENU_ID ASC")
    @Results(value = {@Result(column = "MENU_ID", property = "value"), @Result(column = "MENU_NM", property = "label")})
    List<GetValueLabeVOListResponseVO> getPMenuValueLableVOList() throws CommonException;
}
