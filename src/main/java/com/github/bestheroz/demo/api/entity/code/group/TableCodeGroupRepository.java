package com.github.bestheroz.demo.api.entity.code.group;

import com.github.bestheroz.standard.common.mybatis.SqlRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TableCodeGroupRepository extends SqlRepository<TableCodeGroupEntity> {
}
