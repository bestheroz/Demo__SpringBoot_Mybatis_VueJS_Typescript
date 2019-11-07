package com.github.bestheroz.sample.api.tablevo.samplemembermst;

import com.github.bestheroz.standard.common.exception.CommonException;
import com.github.bestheroz.standard.common.tablevo.SqlForTableDAO;
import com.github.bestheroz.standard.common.tablevo.SqlForTableVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Mapper
@Repository
public interface TableSampleMemberMstDAO extends SqlForTableDAO {

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT)
    List<TableSampleMemberMstVO> getList(final TableSampleMemberMstVO vo, final Set<String> whereKeys, final String orderByColumns) throws CommonException;

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT_ONE)
    TableSampleMemberMstVO getOne(final TableSampleMemberMstVO vo, final Set<String> whereKeys) throws CommonException;
}
