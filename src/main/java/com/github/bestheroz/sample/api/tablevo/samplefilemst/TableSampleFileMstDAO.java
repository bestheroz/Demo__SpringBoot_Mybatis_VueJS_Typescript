package com.github.bestheroz.sample.api.tablevo.samplefilemst;

import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.tablevo.SqlForTableDAO;
import com.github.bestheroz.standard.common.tablevo.SqlForTableVO;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Mapper
@Repository
public interface TableSampleFileMstDAO extends SqlForTableDAO {
    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT)
    List<TableSampleFileMstVO> getList(final TableSampleFileMstVO vo, final Set<String> whereKeys, final String orderByColumns) throws CommonException;

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT_ONE)
    TableSampleFileMstVO getOne(final TableSampleFileMstVO vo, final Set<String> whereKeys) throws CommonException;

    @Override
    @InsertProvider(type = SqlForTableVO.class, method = SqlForTableVO.INSERT)
    @SelectKey(statement = "SELECT SEQ_SAMPLE_FILE_MST.NEXTVAL FROM DUAL", keyProperty = "fileSeq", before = true, resultType = Integer.class)
    <T> void insert(final T vo) throws CommonException;
}
