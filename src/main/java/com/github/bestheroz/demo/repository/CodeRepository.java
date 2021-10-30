package com.github.bestheroz.demo.repository;

import com.github.bestheroz.demo.entity.Code;
import com.github.bestheroz.standard.common.mybatis.SqlRepository;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CodeRepository extends SqlRepository<Code> {
  @Select(
      value =
          """
            select distinct type
              from code
             where available = true
             """)
  List<String> getTypes();
}
