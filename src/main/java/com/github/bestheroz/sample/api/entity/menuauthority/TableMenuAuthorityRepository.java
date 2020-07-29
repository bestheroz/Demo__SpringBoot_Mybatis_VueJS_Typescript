package com.github.bestheroz.sample.api.entity.menuauthority;

import com.github.bestheroz.standard.common.repository.SqlRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TableMenuAuthorityRepository extends SqlRepository<TableMenuAuthorityEntity, Integer> {
}
