package com.github.bestheroz.sample.api.menu;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuDAO {
    List<MenuVO> getList(final Integer authority);
}
