package com.github.bestheroz.standard.common.mybatis;

import com.github.bestheroz.standard.common.util.MapperUtils;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface SqlRepository<T extends Serializable> {
  default List<T> getItems() {
    return this.getItemsByMapWithOrder(Map.of(), List.of());
  }

  default List<T> getItemsWithOrder(final List<String> orderByConditions) {
    return this.getItemsByMapWithOrder(Map.of(), orderByConditions);
  }

  default List<T> getItemsByMap(final Map<String, Object> whereConditions) {
    return this.getItemsByMapWithOrder(whereConditions, List.of());
  }

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_ITEMS_BY_MAP_WITH_ORDER)
  List<T> getItemsByMapWithOrder(
      final Map<String, Object> whereConditions, List<String> orderByConditions);

  // Target 시리즈를 사용하기 위해서는 Entity에 반드시 @NoArgsConstructor 가 필요하다
  default List<T> getTargetItems(final Set<String> targetColumns) {
    return this.getTargetItemsByMapWithOrder(targetColumns, Map.of(), List.of());
  }

  // Target 시리즈를 사용하기 위해서는 Entity에 반드시 @NoArgsConstructor 가 필요하다
  default List<T> getTargetItemsWithOrder(
      final Set<String> targetColumns, final List<String> orderByConditions) {
    return this.getTargetItemsByMapWithOrder(targetColumns, Map.of(), orderByConditions);
  }

  // Target 시리즈를 사용하기 위해서는 Entity에 반드시 @NoArgsConstructor 가 필요하다
  default List<T> getTargetItemsByMap(
      final Set<String> targetColumns, final Map<String, Object> whereConditions) {
    return this.getTargetItemsByMapWithOrder(targetColumns, whereConditions, List.of());
  }

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

  default int countAll() {
    return this.countByMap(Map.of());
  }

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.COUNT_BY_MAP)
  int countByMap(final Map<String, Object> whereConditions);

  @InsertProvider(type = SqlCommand.class, method = SqlCommand.INSERT)
  // @SelectKey(statement = "SELECT SEQSEQSEQSEQ.NEXTVAL FROM DUAL", keyProperty = "seq", before =
  // true, resultType = Long.class)
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(final T entity);

  @InsertProvider(type = SqlCommand.class, method = SqlCommand.INSERT_BATCH)
  void insertBatch(final List<T> entities);

  default void updateById(final T entity, final Long id) {
    this.updateMapByMap(MapperUtils.toMap(entity), Map.of("id", id));
  }

  default void updateByMap(final T entity, final Map<String, Object> whereConditions) {
    this.updateMapByMap(MapperUtils.toMap(entity), whereConditions);
  }

  @UpdateProvider(type = SqlCommand.class, method = SqlCommand.UPDATE_MAP_BY_MAP)
  void updateMapByMap(
      final Map<String, Object> updateMap, final Map<String, Object> whereConditions);

  default void updateMapById(final Map<String, Object> updateMap, final Long id) {
    this.updateMapByMap(updateMap, Map.of("id", id));
  }

  @DeleteProvider(type = SqlCommand.class, method = SqlCommand.DELETE_BY_MAP)
  void deleteByMap(final Map<String, Object> whereConditions);

  default void deleteById(final Long id) {
    this.deleteByMap(Map.of("id", id));
  }

  // vuetify DataTable 규격에 맞춰진 스펙
  @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_ITEMS_BY_DATATABLE)
  List<T> getItemsForDataTable(final DataTableFilterDTO dataTableFilterDTO);

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.COUNT_BY_DATATABLE)
  int countForDataTable(final DataTableFilterDTO dataTableFilterDTO);
}
