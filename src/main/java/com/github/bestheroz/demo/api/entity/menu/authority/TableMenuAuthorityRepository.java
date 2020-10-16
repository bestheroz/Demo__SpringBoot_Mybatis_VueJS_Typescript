package com.github.bestheroz.demo.api.entity.menu.authority;

import com.github.bestheroz.standard.common.mybatis.SqlRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TableMenuAuthorityRepository extends SqlRepository<TableMenuAuthorityEntity> {
}
