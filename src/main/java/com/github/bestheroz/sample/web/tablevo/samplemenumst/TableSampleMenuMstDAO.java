package com.github.bestheroz.sample.web.tablevo.samplemenumst;

import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.tablevo.SqlForTableVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TableSampleMenuMstDAO {
    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.COUNT)
    int countSampleMenuMst(final TableSampleMenuMstVO vo, List<String> whereKey) throws CommonException;

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT)
    List<TableSampleMenuMstVO> getSampleMenuMstVOList(final TableSampleMenuMstVO vo, List<String> whereKey, String orderByColumns) throws CommonException;

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT_ONE)
    TableSampleMenuMstVO getSampleMenuMstVO(final TableSampleMenuMstVO vo, List<String> whereKey) throws CommonException;

    @InsertProvider(type = SqlForTableVO.class, method = SqlForTableVO.INSERT)
    @SelectKey(statement = "SELECT SEQ_SAMPLE_MENU_MST.NEXTVAL FROM DUAL", keyProperty = "menuId", before = true, resultType = Integer.class)
    void insertSampleMenuMst(final TableSampleMenuMstVO vo) throws CommonException;

    @UpdateProvider(type = SqlForTableVO.class, method = SqlForTableVO.UPDATE)
    void updateSampleMenuMst(final TableSampleMenuMstVO vo, List<String> whereKey, List<String> forcedUpdateKey) throws CommonException;

    @DeleteProvider(type = SqlForTableVO.class, method = SqlForTableVO.DELETE)
    void deleteSampleMenuMst(final TableSampleMenuMstVO vo, List<String> whereKey) throws CommonException;
}
