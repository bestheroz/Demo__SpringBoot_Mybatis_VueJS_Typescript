package com.github.bestheroz.sample.api.entity.code;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableCodeRepository extends CrudRepository<TableCodeEntity, TableCodeEntityId> {
    List<TableCodeEntity> findByCodeGroup(String id);

    void deleteByCodeGroup(String id);
}
