package com.github.bestheroz.sample.api.tablevo.samplemenumst;

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
public interface TableSampleMenuMstDAO extends SqlForTableDAO {

    @SuppressWarnings("unused")
    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT)
    List<TableSampleMenuMstVO> getList(final TableSampleMenuMstVO vo, final Set<String> whereKeys, final String orderByColumns) throws CommonException;

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT_ONE)
    TableSampleMenuMstVO getOne(final TableSampleMenuMstVO vo, final Set<String> whereKeys) throws CommonException;

    @Override
    @InsertProvider(type = SqlForTableVO.class, method = SqlForTableVO.INSERT)
    @SelectKey(statement = "SELECT SEQ_SAMPLE_MENU_MST.NEXTVAL FROM DUAL", keyProperty = "menuId", before = true, resultType = Integer.class)
    <T> void insert(final T vo) throws CommonException;

}
