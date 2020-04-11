package com.github.bestheroz.sample.api.entity.samplemenumst;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TableSampleMenuMstRepository extends JpaRepository<TableSampleMenuMstVO, Integer> {
}
