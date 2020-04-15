package com.github.bestheroz.sample.api.entity.code;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableCodeRepository extends JpaRepository<TableCodeVO, TableCodeVOId> {
    List<TableCodeVO> findByCodeGroup(String id);

    void deleteByCodeGroup(String id);
}
