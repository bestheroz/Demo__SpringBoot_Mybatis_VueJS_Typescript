package com.github.bestheroz.sample.api.admin.menu;

import com.github.bestheroz.standard.common.code.CodeVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMenuDAO {
    @Query(value = "SELECT SMM.ID AS VALUE, SMM.NAME AS TEXT FROM MENU SMM WHERE SMM.TYPE = 'G' ORDER BY SMM.ID ASC", nativeQuery = true)
    List<CodeVO> getMenuTypeG() throws BusinessException;
}
