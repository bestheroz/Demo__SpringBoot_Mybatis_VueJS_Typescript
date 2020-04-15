package com.github.bestheroz.sample.api.entity.codegroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableCodeGroupRepository extends JpaRepository<TableCodeGroupVO, String> {
}
