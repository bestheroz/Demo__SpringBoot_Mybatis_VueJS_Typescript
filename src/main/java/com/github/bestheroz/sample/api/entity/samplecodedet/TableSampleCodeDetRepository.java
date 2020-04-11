package com.github.bestheroz.sample.api.entity.samplecodedet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableSampleCodeDetRepository extends JpaRepository<TableSampleCodeDetVO, TableSampleCodeDetId> {
    List<TableSampleCodeDetVO> findByGroupCode(String id);

    void deleteByGroupCode(String id);
}
