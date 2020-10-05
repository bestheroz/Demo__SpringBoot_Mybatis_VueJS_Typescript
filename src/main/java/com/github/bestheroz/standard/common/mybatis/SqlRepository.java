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

  @SelectProvider(
    type = SqlCommand.class,
    method = SqlCommand.SELECT_ITEMS_WITH_ORDER
  )
  List<T> getItemsWithOrder(final Set<String> orderByConditions);

  @SelectProvider(
    type = SqlCommand.class,
    method = SqlCommand.SELECT_ITEMS_BY_KEY
  )
  List<T> getItemsByKey(final Map<String, Object> whereConditions);

  @SelectProvider(
    type = SqlCommand.class,
    method = SqlCommand.SELECT_ITEMS_BY_KEY_WITH_ORDER
  )
  List<T> getItemsByKeyWithOrder(
    final Map<String, Object> whereConditions,
    Set<String> orderByConditions
  );

  @SelectProvider(
    type = SqlCommand.class,
    method = SqlCommand.SELECT_ITEM_BY_KEY
  )
  Optional<T> getItemByKey(final Map<String, Object> whereConditions);

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.COUNT_ALL)
  int countAll();

  @SelectProvider(type = SqlCommand.class, method = SqlCommand.COUNT_BY_KEY)
  int countByKey(final Map<String, Object> whereConditions);

  @InsertProvider(type = SqlCommand.class, method = SqlCommand.INSERT)
  // @SelectKey(statement = "SELECT SEQSEQSEQSEQ.NEXTVAL FROM DUAL", keyProperty = "seq", before = true, resultType = Long.class)
  void insert(final T entity);

  @UpdateProvider(type = SqlCommand.class, method = SqlCommand.UPDATE_BY_KEY)
  void updateByKey(final T entity, final Map<String, Object> whereConditions);

  @UpdateProvider(
    type = SqlCommand.class,
    method = SqlCommand.UPDATE_MAP_BY_KEY
  )
  void updateMapByKey(
    final Map<String, Object> updateMap,
    final Map<String, Object> whereConditions
  );

  @DeleteProvider(type = SqlCommand.class, method = SqlCommand.DELETE_BY_KEY)
  void deleteByKey(final Map<String, Object> whereConditions);
}
