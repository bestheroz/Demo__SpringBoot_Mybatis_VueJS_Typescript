package com.github.bestheroz.sample.api.entity.menu;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableMenuRepository extends CrudRepository<TableMenuEntity, Integer> {

    @Query(value = "SELECT M.*\n" +
            "FROM MENU M\n" +
            "WHERE M.ID = 1\n" +
            "ORDER BY M.DISPLAY_ORDER, M.NAME", nativeQuery = true)
    List<TableMenuEntity> findAllByLevel1();

    @Query(value = "SELECT M.*\n" +
            "FROM MENU M\n" +
            "         JOIN MENU_AUTHORITY MA ON INSTR(MA.MENU_ID_LIST, CONCAT('^|', M.ID, ',')) > 0\n" +
            "WHERE M.PARENT_ID = 1\n" +
            "  AND MA.AUTHORITY = :authority\n" +
            "ORDER BY M.DISPLAY_ORDER, M.NAME", nativeQuery = true)
    List<TableMenuEntity> findAllByLevel2AndAuthority(@Param("authority") Integer authority);

    @Query(value = "SELECT M.*\n" +
            "FROM MENU M\n" +
            "WHERE M.PARENT_ID = 1\n" +
            "ORDER BY M.DISPLAY_ORDER, M.NAME", nativeQuery = true)
    List<TableMenuEntity> findAllByLevel2();

    @Query(value = "SELECT M2.*\n" +
            "FROM MENU M\n" +
            "         JOIN MENU M2 ON M2.PARENT_ID = M.ID\n" +
            "         JOIN MENU_AUTHORITY MA ON INSTR(MA.MENU_ID_LIST, CONCAT('^|', M2.ID, ',')) > 0\n" +
            "    AND M.PARENT_ID = 1\n" +
            "    AND MA.AUTHORITY = :authority\n" +
            "    AND M2.PARENT_ID = :parentId\n" +
            "ORDER BY M2.DISPLAY_ORDER, M2.NAME", nativeQuery = true)
    List<TableMenuEntity> findAllByLevel3AndAuthorityAndParentId(@Param("authority") Integer authority, @Param("parentId") Integer parentId);

    @Query(value = "SELECT M2.*\n" +
            "FROM MENU M\n" +
            "         JOIN MENU M2 ON M2.PARENT_ID = M.ID\n" +
            "    AND M.PARENT_ID = 1\n" +
            "    AND M2.PARENT_ID = :parentId\n" +
            "ORDER BY M2.DISPLAY_ORDER, M2.NAME", nativeQuery = true)
    List<TableMenuEntity> findAllByLevel3AndParentId(@Param("parentId") Integer parentId);
}
