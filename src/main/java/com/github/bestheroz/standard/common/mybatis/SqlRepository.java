package com.github.bestheroz.standard.common.mybatis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface SqlRepository<T extends Serializable> {
  @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_ITEMS)
  List<T> getItems();

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_ITEMS_WITH_ORDER)
  List<T> getItemsWithOrder(final List<String> orderByConditions);

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_ITEMS_BY_MAP)
  List<T> getItemsByMap(final Map<String, Object> whereConditions);

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_ITEMS_BY_MAP_WITH_ORDER)
  List<T> getItemsByMapWithOrder(
      final Map<String, Object> whereConditions, List<String> orderByConditions);

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_TARGET_ITEMS)
  // Target 시리즈를 사용하기 위해서는 Entity에 반드시 @NoArgsConstructor 가 필요하다
  List<T> getTargetItems(final Set<String> targetColumns);

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_TARGET_ITEMS_WITH_ORDER)
  // Target 시리즈를 사용하기 위해서는 Entity에 반드시 @NoArgsConstructor 가 필요하다
  List<T> getTargetItemsWithOrder(
      final Set<String> targetColumns, final List<String> orderByConditions);

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_TARGET_ITEMS_BY_MAP)
  // Target 시리즈를 사용하기 위해서는 Entity에 반드시 @NoArgsConstructor 가 필요하다
  List<T> getTargetItemsByMap(
      final Set<String> targetColumns, final Map<String, Object> whereConditions);

  @SelectProvider(
      type = SqlCommand.class,
      method = SqlCommand.SELECT_TARGET_ITEMS_BY_MAP_WITH_ORDER)
  // Target 시리즈를 사용하기 위해서는 Entity에 반드시 @NoArgsConstructor 가 필요하다
  List<T> getTargetItemsByMapWithOrder(
      final Set<String> targetColumns,
      final Map<String, Object> whereConditions,
      List<String> orderByConditions);

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_ITEM_BY_MAP)
  Optional<T> getItemByMap(final Map<String, Object> whereConditions);

  default Optional<T> getItemById(final Long id) {
    return this.getItemByMap(Map.of("id", id));
  }

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_ITEMS_BY_DATATABLE)
  List<T> getItemsForDataTable(final DataTableFilterDTO dataTableFilterDTO);

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_ITEMS_BY_SEARCH_AND_DATATABLE)
  List<T> getItemsForSearchAndDataTable(
      String search, Set<String> targetColumns, final DataTableFilterDTO dataTableFilterDTO);

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.COUNT_ALL)
  int countAll();

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.COUNT_BY_DATATABLE)
  int countForDataTable(final DataTableFilterDTO dataTableFilterDTO);

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.COUNT_BY_SEARCH_AND_DATATABLE)
  int countForSearchAndDataTable(
      String search, Set<String> targetColumns, final DataTableFilterDTO dataTableFilterDTO);

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.COUNT_BY_MAP)
  int countByMap(final Map<String, Object> whereConditions);

  @InsertProvider(type = SqlCommand.class, method = SqlCommand.INSERT)
  // @SelectKey(statement = "SELECT SEQSEQSEQSEQ.NEXTVAL FROM DUAL", keyProperty = "seq", before =
  // true, resultType = Long.class)
  //  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(final T entity);

  @InsertProvider(type = SqlCommand.class, method = SqlCommand.INSERT_BATCH)
  void insertBatch(final List<T> entities);

  default void updateById(final T entity, final Long id) {
    this.updateByMap(entity, Map.of("id", id));
  }

  @UpdateProvider(type = SqlCommand.class, method = SqlCommand.UPDATE_BY_MAP)
  void updateByMap(final T entity, final Map<String, Object> whereConditions);

  @UpdateProvider(type = SqlCommand.class, method = SqlCommand.UPDATE_MAP_BY_MAP)
  void updateMapByMap(
      final Map<String, Object> updateMap, final Map<String, Object> whereConditions);

  @DeleteProvider(type = SqlCommand.class, method = SqlCommand.DELETE_BY_MAP)
  void deleteByMap(final Map<String, Object> whereConditions);

  default void deleteById(final Long id) {
    this.deleteByMap(Map.of("id", id));
  }
}
