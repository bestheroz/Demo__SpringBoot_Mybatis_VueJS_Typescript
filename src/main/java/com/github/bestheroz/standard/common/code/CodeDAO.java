package com.github.bestheroz.standard.common.code;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeDAO extends CrudRepository<CodeVO, String> {
    @Query(value = "SELECT SCD.CODE AS VALUE, SCD.NAME AS TEXT FROM CODE SCD WHERE SCD.IS_USING = 'Y' AND SCD.CODE_GROUP = :codeGroup ORDER BY SCD.DISPLAY_ORDER ASC",
            nativeQuery = true)
    List<CodeVO> getCodeVOList(@Param("codeGroup") final String codeGroup);
}
