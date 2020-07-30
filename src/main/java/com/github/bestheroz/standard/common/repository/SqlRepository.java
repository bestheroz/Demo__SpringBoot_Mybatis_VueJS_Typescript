package com.github.bestheroz.standard.common.repository;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface SqlRepository<T> {

    @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_ITEMS)
    List<T> getItems(Class<T> tClass);

    @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_ITEMS_WITH_ORDER)
    List<T> getItemsWithOrder(Class<T> tClass, final Set<String> orderByConditions);

    @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_ITEMS_BY_KEY)
    List<T> getItemsByKey(Class<T> tClass, final Map<String, Object> whereConditions);

    @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_ITEMS_BY_KEY_WITH_ORDER)
    List<T> getItemsByKeyWithOrder(Class<T> tClass, final Map<String, Object> whereConditions, Set<String> orderByConditions);

    @SelectProvider(type = SqlCommand.class, method = SqlCommand.SELECT_ITEM)
    Optional<T> getItem(Class<T> tClass, final Map<String, Object> whereConditions);

    @SelectProvider(type = SqlCommand.class, method = SqlCommand.COUNT_ALL)
    int countAll(Class<T> tClass);

    @SelectProvider(type = SqlCommand.class, method = SqlCommand.COUNT_BY_KEY)
    int countByKey(Class<T> tClass, final Map<String, Object> whereConditions);

    @InsertProvider(type = SqlCommand.class, method = SqlCommand.INSERT)
        // @SelectKey(statement = "SELECT SEQSEQSEQSEQ.NEXTVAL FROM DUAL", keyProperty = "seq", before = true, resultType = Long.class)
    void insert(final T entity);

    @UpdateProvider(type = SqlCommand.class, method = SqlCommand.UPDATE_BY_KEY)
    void update(final T entity, final Map<String, Object> whereConditions);

    @UpdateProvider(type = SqlCommand.class, method = SqlCommand.UPDATE_MAP_BY_KEY)
    void updateMap(final Class<T> tClass, final Map<String, Object> updateMap, final Map<String, Object> whereConditions);

    @DeleteProvider(type = SqlCommand.class, method = SqlCommand.DELETE_BY_KEY)
    void delete(Class<T> tClass, final Map<String, Object> whereConditions);
}
