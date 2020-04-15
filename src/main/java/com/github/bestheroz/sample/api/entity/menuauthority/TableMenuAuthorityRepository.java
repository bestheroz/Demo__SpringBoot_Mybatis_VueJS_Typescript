package com.github.bestheroz.sample.api.entity.menuauthority;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableMenuAuthorityRepository extends JpaRepository<TableMenuAuthorityVO, String> {
}
