package com.github.bestheroz.standard.common.enccolumn;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EncColumnDAO {
    @Select(value = "SELECT FNC_GET_DECRYPT(#{column, jdbcType=VARCHAR}) FROM DUAL")
    String getDecryptColumn(final String column);

    @Select(value = "SELECT FNC_GET_ENCRYPT(#{column, jdbcType=VARCHAR}) FROM DUAL")
    String getEncryptColumn(final String column);
}
