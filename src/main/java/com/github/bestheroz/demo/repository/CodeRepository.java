package com.github.bestheroz.demo.repository;

import com.github.bestheroz.demo.entity.Code;
import com.github.bestheroz.standard.common.mybatis.SqlRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CodeRepository extends SqlRepository<Code> {}
