package com.github.bestheroz.sample.api.menu;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MenuDAO {
    List<MenuVO> getList(final Integer authority);
}
