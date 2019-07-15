package com.github.bestheroz.sample.web.tablevo.samplecodedet;

import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.tablevo.SqlForTableVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TableSampleCodeDetDAO {
    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.COUNT)
    int countSampleCodeDet(final TableSampleCodeDetVO vo, List<String> whereKey) throws CommonException;

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT)
    List<TableSampleCodeDetVO> getSampleCodeDetVOList(final TableSampleCodeDetVO vo, List<String> whereKey, String orderByColumns) throws CommonException;

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT_ONE)
    TableSampleCodeDetVO getSampleCodeDetVO(final TableSampleCodeDetVO vo, List<String> whereKey) throws CommonException;

    @InsertProvider(type = SqlForTableVO.class, method = SqlForTableVO.INSERT)
    void insertSampleCodeDet(final TableSampleCodeDetVO vo) throws CommonException;

    @UpdateProvider(type = SqlForTableVO.class, method = SqlForTableVO.UPDATE)
    void updateSampleCodeDet(final TableSampleCodeDetVO vo, List<String> whereKey, List<String> forcedUpdateKey) throws CommonException;

    @DeleteProvider(type = SqlForTableVO.class, method = SqlForTableVO.DELETE)
    void deleteSampleCodeDet(final TableSampleCodeDetVO vo, List<String> whereKey) throws CommonException;
}
