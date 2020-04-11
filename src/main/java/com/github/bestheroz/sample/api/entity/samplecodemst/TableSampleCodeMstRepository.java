package com.github.bestheroz.sample.api.entity.samplecodemst;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableSampleCodeMstRepository extends JpaRepository<TableSampleCodeMstVO, String> {
}
