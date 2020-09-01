package com.github.bestheroz.sample.api.entity.layout;

import com.github.bestheroz.standard.common.mybatis.SqlRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TableLayoutRepository extends SqlRepository<TableLayoutEntity> {
}
