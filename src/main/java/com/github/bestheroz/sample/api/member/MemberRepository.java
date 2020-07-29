package com.github.bestheroz.sample.api.member;

import com.github.bestheroz.standard.common.code.CodeVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.repository.SqlRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MemberRepository extends SqlRepository<CodeVO> {
    @Select(value = "SELECT M.ID AS VALUE, M.NAME AS TEXT FROM MEMBER M ORDER BY M.ID ASC")
    @Cacheable(value = "memberCache")
    List<CodeVO> getCodeItems() throws BusinessException;
}
