package com.github.bestheroz.sample.web.tablevo.samplecodemst;

import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.tablevo.SqlForTableVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TableSampleCodeMstDAO {
    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.COUNT)
    int countSampleCodeMst(final TableSampleCodeMstVO vo, List<String> whereKey) throws CommonException;

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT)
    List<TableSampleCodeMstVO> getSampleCodeMstVOList(final TableSampleCodeMstVO vo, List<String> whereKey, String orderByColumns) throws CommonException;

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT_ONE)
    TableSampleCodeMstVO getSampleCodeMstVO(final TableSampleCodeMstVO vo, List<String> whereKey) throws CommonException;

    @InsertProvider(type = SqlForTableVO.class, method = SqlForTableVO.INSERT)
    void insertSampleCodeMst(final TableSampleCodeMstVO vo) throws CommonException;

    @UpdateProvider(type = SqlForTableVO.class, method = SqlForTableVO.UPDATE)
    void updateSampleCodeMst(final TableSampleCodeMstVO vo, List<String> whereKey, List<String> forcedUpdateKey) throws CommonException;

    @DeleteProvider(type = SqlForTableVO.class, method = SqlForTableVO.DELETE)
    void deleteSampleCodeMst(final TableSampleCodeMstVO vo, List<String> whereKey) throws CommonException;
}
