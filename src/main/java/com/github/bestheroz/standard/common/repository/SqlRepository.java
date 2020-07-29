package com.github.bestheroz.standard.common.repository;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.Optional;

public interface SqlRepository<T, ID> {
    @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT)
    Iterable<T> findAll();

    @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_ONE)
    Optional<T> findById(ID id);

    @SelectProvider(type = SqlCommand.class, method = SqlCommand.COUNT)
    int count();

    @InsertProvider(type = SqlCommand.class, method = SqlCommand.SAVE)
        // @SelectKey(statement = "SELECT SEQSEQSEQSEQ.NEXTVAL FROM DUAL", keyProperty = "seq", before = true, resultType = Long.class)
    <T> void save(final T vo);

    @DeleteProvider(type = SqlCommand.class, method = SqlCommand.DELETE)
    <T> void deleteById(ID id);
}
