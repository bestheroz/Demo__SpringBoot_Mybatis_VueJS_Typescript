package com.github.bestheroz.sample.api.entity.menu;

import com.github.bestheroz.standard.common.repository.SqlRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TableMenuRepository extends SqlRepository<TableMenuEntity, Integer> {

    @Select(value = "SELECT M.*\n" +
            "FROM MENU M\n" +
            "WHERE M.ID = 1\n" +
            "ORDER BY M.DISPLAY_ORDER, M.NAME")
    List<TableMenuEntity> findAllByLevel1();

    @Select(value = "SELECT M.*\n" +
            "FROM MENU M\n" +
            "         JOIN MENU_AUTHORITY MA ON INSTR(MA.MENU_ID_LIST, CONCAT('^|', M.ID, ',')) > 0\n" +
            "WHERE M.PARENT_ID = 1\n" +
            "  AND MA.AUTHORITY = :authority\n" +
            "ORDER BY M.DISPLAY_ORDER, M.NAME")
    List<TableMenuEntity> findAllByLevel2AndAuthority(Integer authority);

    @Select(value = "SELECT M.*\n" +
            "FROM MENU M\n" +
            "WHERE M.PARENT_ID = 1\n" +
            "ORDER BY M.DISPLAY_ORDER, M.NAME")
    List<TableMenuEntity> findAllByLevel2();

    @Select(value = "SELECT M2.*\n" +
            "FROM MENU M\n" +
            "         JOIN MENU M2 ON M2.PARENT_ID = M.ID\n" +
            "         JOIN MENU_AUTHORITY MA ON INSTR(MA.MENU_ID_LIST, CONCAT('^|', M2.ID, ',')) > 0\n" +
            "    AND M.PARENT_ID = 1\n" +
            "    AND MA.AUTHORITY = :authority\n" +
            "    AND M2.PARENT_ID = :parentId\n" +
            "ORDER BY M2.DISPLAY_ORDER, M2.NAME")
    List<TableMenuEntity> findAllByLevel3AndAuthorityAndParentId(Integer authority, Integer parentId);

    @Select(value = "SELECT M2.*\n" +
            "FROM MENU M\n" +
            "         JOIN MENU M2 ON M2.PARENT_ID = M.ID\n" +
            "    AND M.PARENT_ID = 1\n" +
            "    AND M2.PARENT_ID = :parentId\n" +
            "ORDER BY M2.DISPLAY_ORDER, M2.NAME")
    List<TableMenuEntity> findAllByLevel3AndParentId(Integer parentId);
}
