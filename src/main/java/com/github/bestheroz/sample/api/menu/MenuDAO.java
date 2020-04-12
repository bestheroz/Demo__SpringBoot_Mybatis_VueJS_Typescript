package com.github.bestheroz.sample.api.menu;

import com.github.bestheroz.sample.api.entity.samplemenumst.TableSampleMenuMstVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MenuDAO {
    List<TableSampleMenuMstVO> getList(final String levelcod);

}
