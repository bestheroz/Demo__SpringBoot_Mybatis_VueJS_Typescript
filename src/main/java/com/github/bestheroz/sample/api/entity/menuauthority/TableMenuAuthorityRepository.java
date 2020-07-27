package com.github.bestheroz.sample.api.entity.menuauthority;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableMenuAuthorityRepository extends CrudRepository<TableMenuAuthorityEntity, Integer> {
}
