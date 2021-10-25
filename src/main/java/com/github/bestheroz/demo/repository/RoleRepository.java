package com.github.bestheroz.demo.repository;

import com.github.bestheroz.demo.entity.Role;
import com.github.bestheroz.standard.common.mybatis.SqlCommand;
import com.github.bestheroz.standard.common.mybatis.SqlRepository;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RoleRepository extends SqlRepository<Role> {
  @Override
  @InsertProvider(type = SqlCommand.class, method = SqlCommand.INSERT)
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(final Role entity);
}
