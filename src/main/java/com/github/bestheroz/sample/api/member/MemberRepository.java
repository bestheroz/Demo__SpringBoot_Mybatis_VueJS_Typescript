package com.github.bestheroz.sample.api.member;

import com.github.bestheroz.standard.common.code.CodeVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends CrudRepository<CodeVO, String> {
    @Override
    @Query(value = "SELECT M.ID AS VALUE, M.NAME AS TEXT FROM MEMBER M ORDER BY M.ID ASC", nativeQuery = true)
    @Cacheable(value = "memberCache")
    List<CodeVO> findAll() throws BusinessException;
}
