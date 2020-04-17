package com.github.bestheroz.sample.api.entity.menu;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableMenuRepository extends CrudRepository<TableMenuVO, Integer> {
}
