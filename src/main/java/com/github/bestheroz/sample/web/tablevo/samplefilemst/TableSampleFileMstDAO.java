package com.github.bestheroz.sample.web.tablevo.samplefilemst;

import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.tablevo.SqlForTableVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TableSampleFileMstDAO {
    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.COUNT)
    int countSampleFileMst(final TableSampleFileMstVO vo, List<String> whereKey) throws CommonException;

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT)
    List<TableSampleFileMstVO> getSampleFileMstVOList(final TableSampleFileMstVO vo, List<String> whereKey, String orderByColumns) throws CommonException;

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT_ONE)
    TableSampleFileMstVO getSampleFileMstVO(final TableSampleFileMstVO vo, List<String> whereKey) throws CommonException;

    @InsertProvider(type = SqlForTableVO.class, method = SqlForTableVO.INSERT)
    @SelectKey(statement = "SELECT SEQ_SAMPLE_FILE_MST.NEXTVAL FROM DUAL", keyProperty = "fileSeq", before = true, resultType = Integer.class)
    void insertSampleFileMst(final TableSampleFileMstVO vo) throws CommonException;

    @UpdateProvider(type = SqlForTableVO.class, method = SqlForTableVO.UPDATE)
    void updateSampleFileMst(final TableSampleFileMstVO vo, List<String> whereKey, List<String> forcedUpdateKey) throws CommonException;

    @DeleteProvider(type = SqlForTableVO.class, method = SqlForTableVO.DELETE)
    void deleteSampleFileMst(final TableSampleFileMstVO vo, List<String> whereKey) throws CommonException;
}
