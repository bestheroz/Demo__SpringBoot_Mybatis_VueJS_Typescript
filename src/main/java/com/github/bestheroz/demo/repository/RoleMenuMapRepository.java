package com.github.bestheroz.demo.repository;

import com.github.bestheroz.demo.entity.RoleMenuMap;
import com.github.bestheroz.standard.common.mybatis.SqlCommand;
import com.github.bestheroz.standard.common.mybatis.SqlRepository;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RoleMenuMapRepository extends SqlRepository<RoleMenuMap> {
  @Override
  @InsertProvider(type = SqlCommand.class, method = SqlCommand.INSERT)
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(final RoleMenuMap RoleMenuMap);
}
