package com.github.bestheroz.sample.web.admin.menu;

import com.github.bestheroz.sample.web.admin.menu.response.GetSampleMenuMstVOListResponseVO;
import com.github.bestheroz.sample.web.tablevo.samplemenumst.TableSampleMenuMstVO;
import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.tablevo.SqlForTableVO;
import com.github.bestheroz.standard.common.valuelabel.response.GetValueLabeVOListResponseVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

@Mapper
public interface AdminMenuDAO {
    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT)
    List<GetSampleMenuMstVOListResponseVO> getList(final TableSampleMenuMstVO vo, final Set<String> whereKeys, final String orderByColumns) throws CommonException;

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT_ONE)
    GetSampleMenuMstVOListResponseVO getVO(final TableSampleMenuMstVO vo, final Set<String> whereKeys) throws CommonException;

    @Select("SELECT SMM.MENU_ID, SMM.MENU_NM FROM SAMPLE_MENU_MST SMM WHERE SMM.MENU_TYP = 'G' ORDER BY SMM.MENU_ID ASC")
    @Results(value = {@Result(column = "MENU_ID", property = "value"), @Result(column = "MENU_NM", property = "label")})
    List<GetValueLabeVOListResponseVO> getMenuTypG() throws CommonException;
}
