package com.github.bestheroz.sample.api.entity.code;

import com.github.bestheroz.standard.common.repository.SqlRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TableCodeRepository extends SqlRepository<TableCodeEntity, TableCodeEntityId> {
    List<TableCodeEntity> findAllByCodeGroup(String id);

    void deleteByCodeGroup(String id);
}
