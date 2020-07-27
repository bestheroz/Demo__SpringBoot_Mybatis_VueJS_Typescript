package com.github.bestheroz.sample.api.entity.codegroup;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableCodeGroupRepository extends CrudRepository<TableCodeGroupEntity, String> {
}
