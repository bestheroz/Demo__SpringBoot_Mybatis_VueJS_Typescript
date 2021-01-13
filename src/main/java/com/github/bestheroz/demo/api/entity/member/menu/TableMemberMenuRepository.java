package com.github.bestheroz.demo.api.entity.member.menu;

import com.github.bestheroz.standard.common.mybatis.SqlRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TableMemberMenuRepository extends SqlRepository<TableMemberMenuEntity> {}
