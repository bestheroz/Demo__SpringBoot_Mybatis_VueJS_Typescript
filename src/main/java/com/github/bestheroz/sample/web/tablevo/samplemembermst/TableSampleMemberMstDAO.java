package com.github.bestheroz.sample.web.tablevo.samplemembermst;

import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.tablevo.SqlForTableVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TableSampleMemberMstDAO {
    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.COUNT)
    int countSampleMemberMst(final TableSampleMemberMstVO vo, List<String> whereKey) throws CommonException;

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT)
    List<TableSampleMemberMstVO> getSampleMemberMstVOList(final TableSampleMemberMstVO vo, List<String> whereKey, String orderByColumns) throws CommonException;

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT_ONE)
    TableSampleMemberMstVO getSampleMemberMstVO(final TableSampleMemberMstVO vo, List<String> whereKey) throws CommonException;

    @InsertProvider(type = SqlForTableVO.class, method = SqlForTableVO.INSERT)
    void insertSampleMemberMst(final TableSampleMemberMstVO vo) throws CommonException;

    @UpdateProvider(type = SqlForTableVO.class, method = SqlForTableVO.UPDATE)
    void updateSampleMemberMst(final TableSampleMemberMstVO vo, List<String> whereKey, List<String> forcedUpdateKey) throws CommonException;

    @DeleteProvider(type = SqlForTableVO.class, method = SqlForTableVO.DELETE)
    void deleteSampleMemberMst(final TableSampleMemberMstVO vo, List<String> whereKey) throws CommonException;
}
