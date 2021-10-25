package com.github.bestheroz.standard.common.mybatis;

import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import com.github.bestheroz.standard.common.util.CaseUtils;
import com.github.bestheroz.standard.common.util.MapperUtils;
import com.github.bestheroz.standard.common.util.NullUtils;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.lang.NonNull;

@Slf4j
public class SqlCommand {
  public static final String SELECT_ITEMS = "getItems";
  public static final String SELECT_ITEMS_WITH_ORDER = "getItemsWithOrder";
  public static final String SELECT_ITEMS_BY_MAP = "getItemsByMap";
  public static final String SELECT_ITEMS_BY_MAP_WITH_ORDER = "getItemsByMapWithOrder";
  public static final String SELECT_TARGET_ITEMS = "getTargetItems";
  public static final String SELECT_TARGET_ITEMS_WITH_ORDER = "getTargetItemsWithOrder";
  public static final String SELECT_TARGET_ITEMS_BY_MAP = "getTargetItemsByMap";
  public static final String SELECT_TARGET_ITEMS_BY_MAP_WITH_ORDER = "getTargetItemsByMapWithOrder";
  public static final String SELECT_ITEMS_BY_DATATABLE = "getItemsForDataTable";
  public static final String SELECT_ITEMS_BY_SEARCH_AND_DATATABLE = "getItemsForSearchAndDataTable";
  public static final String SELECT_ITEM_BY_MAP = "getItemByMap";
  public static final String SELECT_ITEM_BY_ID = "getItemById";
  public static final String COUNT_ALL = "countAll";
  public static final String COUNT_BY_DATATABLE = "countForDataTable";
  public static final String COUNT_BY_SEARCH_AND_DATATABLE = "countForSearchAndDataTable";
  public static final String COUNT_BY_MAP = "countByMap";
  public static final String INSERT = "insert";
  public static final String INSERT_BATCH = "insertBatch";
  public static final String UPDATE_BY_MAP = "updateByMap";
  public static final String UPDATE_BY_ID = "updateById";
  public static final String UPDATE_MAP_BY_MAP = "updateMapByMap";
  public static final String DELETE_BY_MAP = "deleteByMap";
  public static final String DELETE_BY_ID = "deleteById";
  public static final Set<String> VARCHAR_JAVA_TYPE_SET = Set.of("String", "Char");
  public static final Set<String> SHORT_JAVA_TYPE_SET = Set.of("Short");
  public static final Set<String> INTEGER_JAVA_TYPE_SET = Set.of("Integer");
  public static final Set<String> BIGINT_JAVA_TYPE_SET = Set.of("Long");
  public static final Set<String> NUMBER_JAVA_TYPE_SET =
      Set.of("Short", "Integer", "Long", "Double");
  public static final Set<String> DOUBLE_JAVA_TYPE_SET = Set.of("Double");
  public static final Set<String> TIMESTAMP_JAVA_TYPE_SET = Set.of("Instant");
  public static final Set<String> BLOB_JAVA_TYPE_SET = Set.of("Byte[]");
  public static final Set<String> BOOLEAN_JAVA_TYPE_SET = Set.of("Boolean", "boolean");
  // 참고용: 각VO에 암호화 컬럼 정의 방법
  private static final String TABLE_COLUMN_NAME_CREATED_BY = "CREATED_BY";
  private static final String TABLE_COLUMN_NAME_CREATED = "CREATED";
  private static final String TABLE_COLUMN_NAME_UPDATED_BY = "UPDATED_BY";
  private static final String TABLE_COLUMN_NAME_UPDATED = "UPDATED";
  private static final String VARIABLE_NAME_CREATED_BY = "createdBy";
  private static final String VARIABLE_NAME_CREATED = "created";
  private static final String VARIABLE_NAME_UPDATED_BY = "updatedBy";
  private static final String VARIABLE_NAME_UPDATED = "updated";
  private static final String SYSDATE = "NOW()";
  public static final Set<String> EXCLUDE_FIELD_SET =
      Set.of("SERIAL_VERSION_U_I_D", "serialVersionUID", "E_N_C_R_Y_P_T_E_D__C_O_L_U_M_N__L_I_S_T");
  private static final String INSERT_STRING = "#'{'{0}{1}'}'";
  private static final String SET_STRING = "{0} = #'{'param{3}.{1}{2}'}'";
  private static final String SET_UPDATED_BY_STRING = "{0} = ''{1}''";
  private static final String WHERE_EQUALS_STRING = "{0} = #'{'whereConditions.{1}{2}'}'";
  private static final String WHERE_NOT_EQUALS_STRING = "{0} <> #'{'whereConditions.{1}{2}'}'";
  private static final String WHERE_CONTAINS_STRING =
      "INSTR({0},  #'{'whereConditions.{1}{2}'}') > 0";
  private static final String WHERE_NOT_CONTAINS_STRING =
      "INSTR({0},  #'{'whereConditions.{1}{2}'}') = 0";
  private static final String WHERE_STARTS_WITH_STRING =
      "INSTR({0},  #'{'whereConditions.{1}{2}'}') = 1";
  private static final String WHERE_ENDS_WITH_STRING =
      "RIGHT({0}, CHAR_LENGTH(#'{'whereConditions.{1}{2}'}')) = #'{'whereConditions.{1}{2}'}'";
  private static final String WHERE_LESS_THAN_STRING = "{0} < #'{'whereConditions.{1}{2}'}'";
  private static final String WHERE_LESS_THAN_EQUALS_STRING =
      "{0} <= #'{'whereConditions.{1}{2}'}'";
  private static final String WHERE_GREATER_THAN_STRING = "{0} > #'{'whereConditions.{1}{2}'}'";
  private static final String WHERE_GREATER_THAN_EQUALS_STRING =
      "{0} >= #'{'whereConditions.{1}{2}'}'";
  private static final String WHERE_IN_STRING = "{0} IN ({1})";
  private static final String WHERE_NOT_IN_STRING = "{0} NOT IN ({1})";

  public String getTableName() {
    return getTableName(this.getEntityClass().getSimpleName());
  }

  public static String getTableName(final String javaClassName) {
    return StringUtils.upperCase(
        CaseUtils.getCamelCaseToSnakeCase(
            StringUtils.substringBetween(javaClassName, "Table", "Entity")));
  }

  private void getSelectSql(final SQL sql, final List<String> columns) {
    columns.forEach(
        column -> {
          final String dbColumnName = CaseUtils.getCamelCaseToSnakeCase(column);
          sql.SELECT(dbColumnName);
        });
  }

  private void verifyWhereKey(final Map<String, Object> whereConditions) {
    if (NullUtils.size(whereConditions) < 1) {
      log.warn("whereConditions is empty");
      throw BusinessException.FAIL_NO_DATA_SUCCESS;
    }
  }

  private void getWhereSql(final SQL sql, final Map<String, Object> whereConditions) {
    whereConditions.forEach(
        (key, value) -> {
          final String columnName = StringUtils.substringBefore(key, ":");
          final String conditionType =
              StringUtils.defaultString(StringUtils.substringAfter(key, ":"), "equals");
          if (StringUtils.equals(conditionType, "in")) {
            sql.WHERE(
                MessageFormat.format(
                    WHERE_IN_STRING,
                    CaseUtils.getCamelCaseToSnakeCase(columnName),
                    StringUtils.defaultIfEmpty(
                        MapperUtils.toArrayList(value, String.class).stream()
                            .map(item -> "'" + item + "'")
                            .collect(Collectors.joining(",")),
                        "''")));
          } else if (conditionType.equals("notIn")) {
            sql.WHERE(
                MessageFormat.format(
                    WHERE_NOT_IN_STRING,
                    CaseUtils.getCamelCaseToSnakeCase(columnName),
                    StringUtils.defaultIfEmpty(
                        MapperUtils.toArrayList(value, String.class).stream()
                            .map(item -> "'" + item + "'")
                            .collect(Collectors.joining(",")),
                        "''")));
          } else {
            final String whereString = this.getWhereString(conditionType);
            sql.WHERE(
                MessageFormat.format(
                    this.getRepositoryMethodParamLength() == 1
                        ? whereString.replace("whereConditions.", "")
                        : whereString,
                    CaseUtils.getCamelCaseToSnakeCase(columnName),
                    columnName,
                    this.getJdbcType(
                        value instanceof String && value.equals("@NULL") ? null : value)));
          }
        });
  }

  private String getWhereString(final String conditionType) {
    final String whereString;
    switch (conditionType) {
      case "contains":
        whereString = WHERE_CONTAINS_STRING;
        break;
      case "notEqual":
      case "booleanNotEqual":
        whereString = WHERE_NOT_EQUALS_STRING;
        break;
      case "notContains":
        whereString = WHERE_NOT_CONTAINS_STRING;
        break;
      case "startsWith":
        whereString = WHERE_STARTS_WITH_STRING;
        break;
      case "endsWith":
        whereString = WHERE_ENDS_WITH_STRING;
        break;
      case "lessThan":
        whereString = WHERE_LESS_THAN_STRING;
        break;
      case "lessThanOrEqual":
        whereString = WHERE_LESS_THAN_EQUALS_STRING;
        break;
      case "greaterThan":
        whereString = WHERE_GREATER_THAN_STRING;
        break;
      case "greaterThanOrEqual":
        whereString = WHERE_GREATER_THAN_EQUALS_STRING;
        break;
      case "in":
        whereString = WHERE_IN_STRING;
        break;
      case "notIn":
        whereString = WHERE_NOT_IN_STRING;
        break;
      default:
        whereString = WHERE_EQUALS_STRING;
        break;
    }
    return whereString;
  }

  public String countAll() {
    return this.countByMap(Map.of());
  }

  public String countByMap(final Map<String, Object> whereConditions) {
    final SQL sql = new SQL();
    sql.SELECT("COUNT(1) AS CNT").FROM(this.getTableName());
    this.getWhereSql(sql, whereConditions);
    log.debug(sql.toString());
    return sql.toString();
  }

  private Class<?> getEntityClass() {
    final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
    final Optional<StackTraceElement> getItems =
        Arrays.stream(stackTrace)
            .filter(
                item -> {
                  try {
                    return (item.getClassName().startsWith("com.sun.proxy.$Proxy")
                        && List.of(
                                SELECT_ITEMS,
                                SELECT_ITEMS_WITH_ORDER,
                                SELECT_ITEMS_BY_MAP,
                                SELECT_ITEMS_BY_MAP_WITH_ORDER,
                                SELECT_TARGET_ITEMS,
                                SELECT_TARGET_ITEMS_WITH_ORDER,
                                SELECT_TARGET_ITEMS_BY_MAP,
                                SELECT_TARGET_ITEMS_BY_MAP_WITH_ORDER,
                                SELECT_ITEMS_BY_DATATABLE,
                                SELECT_ITEM_BY_MAP,
                                COUNT_ALL,
                                COUNT_BY_MAP,
                                COUNT_BY_DATATABLE,
                                INSERT,
                                UPDATE_BY_MAP,
                                UPDATE_MAP_BY_MAP,
                                DELETE_BY_MAP)
                            .contains(item.getMethodName())
                        && Class.forName(item.getClassName()).getInterfaces().length > 0
                        && Class.forName(item.getClassName())
                                .getInterfaces()[0]
                                .getGenericInterfaces()
                                .length
                            > 0);
                  } catch (final ClassNotFoundException e) {
                    log.warn("Failed to getEntityClass");
                    throw BusinessException.ERROR_SYSTEM;
                  }
                })
            .findFirst();
    if (getItems.isEmpty()) {
      throw BusinessException.ERROR_SYSTEM;
    }
    final StackTraceElement item = getItems.get();
    try {
      final Class<?> cInterface = Class.forName(item.getClassName()).getInterfaces()[0];
      return Class.forName(
          StringUtils.substringBetween(
              cInterface.getGenericInterfaces()[0].getTypeName(), "<", ">"));
    } catch (final ClassNotFoundException e) {
      log.warn("Failed to getEntityClass");
      throw BusinessException.ERROR_SYSTEM;
    }
  }

  private int getRepositoryMethodParamLength() {
    final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
    final Optional<StackTraceElement> getItems =
        Arrays.stream(stackTrace)
            .filter(
                item ->
                    (item.getClassName().startsWith("com.sun.proxy.$Proxy")
                        && List.of(
                                SELECT_ITEMS,
                                SELECT_ITEMS_WITH_ORDER,
                                SELECT_ITEMS_BY_MAP,
                                SELECT_ITEMS_BY_MAP_WITH_ORDER,
                                SELECT_TARGET_ITEMS,
                                SELECT_TARGET_ITEMS_WITH_ORDER,
                                SELECT_TARGET_ITEMS_BY_MAP,
                                SELECT_TARGET_ITEMS_BY_MAP_WITH_ORDER,
                                SELECT_ITEMS_BY_DATATABLE,
                                SELECT_ITEM_BY_MAP,
                                COUNT_ALL,
                                COUNT_BY_MAP,
                                COUNT_BY_DATATABLE,
                                INSERT,
                                UPDATE_BY_MAP,
                                UPDATE_MAP_BY_MAP,
                                DELETE_BY_MAP)
                            .contains(item.getMethodName())))
            .findFirst();
    if (getItems.isEmpty()) {
      throw BusinessException.ERROR_SYSTEM;
    }
    final StackTraceElement item = getItems.get();
    try {
      return Arrays.stream(Class.forName(item.getClassName()).getInterfaces()[0].getMethods())
          .filter(method -> method.getName().equals(item.getMethodName()))
          .findFirst()
          .map(first -> first.getParameterTypes().length)
          .orElse(0);
    } catch (final ClassNotFoundException e) {
      log.warn("Failed to getRepositoryMethodParamLength");
      throw BusinessException.ERROR_SYSTEM;
    }
  }

  public String getItems() {
    return this.select(Map.of(), List.of());
  }

  public String getItemsWithOrder(final List<String> orderByConditions) {
    return this.select(Map.of(), orderByConditions);
  }

  public String getItemsByMap(final Map<String, Object> whereConditions) {
    return this.select(whereConditions, List.of());
  }

  public String getItemsByMapWithOrder(
      final Map<String, Object> whereConditions, final List<String> orderByConditions) {
    return this.select(whereConditions, orderByConditions);
  }

  public String getTargetItems(final List<String> targetColumns) {
    return this.select(targetColumns, Map.of(), List.of());
  }

  public String getTargetItemsWithOrder(
      final List<String> targetColumns, final List<String> orderByConditions) {
    return this.select(targetColumns, Map.of(), orderByConditions);
  }

  public String getTargetItemsByMap(
      final List<String> targetColumns, final Map<String, Object> whereConditions) {
    return this.select(targetColumns, whereConditions, List.of());
  }

  public String getTargetItemsByMapWithOrder(
      final List<String> targetColumns,
      final Map<String, Object> whereConditions,
      final List<String> orderByConditions) {
    return this.select(targetColumns, whereConditions, orderByConditions);
  }

  private String select(
      final Map<String, Object> whereConditions, final List<String> orderByConditions) {
    return this.select(this.getEntityFields(), whereConditions, orderByConditions);
  }

  // ordered list required
  private <T> List<String> getEntityFields(final T entity) {
    return Stream.concat(
            Arrays.stream(entity.getClass().getDeclaredFields()).map(Field::getName),
            Arrays.stream(entity.getClass().getSuperclass().getDeclaredFields())
                .map(Field::getName))
        .distinct()
        .filter(fieldName -> !EXCLUDE_FIELD_SET.contains(fieldName))
        .collect(Collectors.toList());
  }

  private List<String> getEntityFields() {
    final Class<?> tClass = this.getEntityClass();
    return Stream.concat(
            Arrays.stream(tClass.getDeclaredFields()).map(Field::getName),
            Arrays.stream(tClass.getSuperclass().getDeclaredFields()).map(Field::getName))
        .distinct()
        .filter(fieldName -> !EXCLUDE_FIELD_SET.contains(fieldName))
        .collect(Collectors.toList());
  }

  private String select(
      final List<String> targetColumns,
      final Map<String, Object> whereConditions,
      final List<String> orderByConditions) {
    final SQL sql = new SQL();
    this.getSelectSql(sql, targetColumns);
    sql.FROM(this.getTableName());
    this.getWhereSql(sql, whereConditions);
    orderByConditions.forEach(columns -> sql.ORDER_BY(CaseUtils.getCamelCaseToSnakeCase(columns)));
    log.debug(sql.toString());
    return sql.toString();
  }

  public String getItemByMap(@NonNull final Map<String, Object> whereConditions) {
    this.verifyWhereKey(whereConditions);
    return this.select(whereConditions, List.of());
  }

  public String getItemById(@NonNull final Long id) {
    return this.select(Map.of("id", id), List.of());
  }

  public <T> String insert(@NonNull final T entity) {
    final Map<String, Object> param = MapperUtils.toMap(entity);
    final SQL sql = new SQL();
    sql.INSERT_INTO(getTableName(entity.getClass().getSimpleName()));
    param.entrySet().stream()
        .filter(
            item ->
                !StringUtils.equalsAny(
                    item.getKey(),
                    VARIABLE_NAME_CREATED,
                    VARIABLE_NAME_CREATED_BY,
                    VARIABLE_NAME_UPDATED,
                    VARIABLE_NAME_UPDATED_BY))
        .forEach(
            item ->
                sql.VALUES(
                    CaseUtils.getCamelCaseToSnakeCase(item.getKey()),
                    MessageFormat.format(
                        INSERT_STRING, item.getKey(), this.getJdbcType(item.getValue()))));

    final List<String> fieldNames = this.getEntityFields(entity);

    if (fieldNames.contains(VARIABLE_NAME_CREATED)) {
      sql.VALUES(TABLE_COLUMN_NAME_CREATED, SYSDATE);
    }
    if (fieldNames.contains(VARIABLE_NAME_UPDATED)) {
      sql.VALUES(TABLE_COLUMN_NAME_UPDATED, SYSDATE);
    }
    if (fieldNames.contains(VARIABLE_NAME_CREATED_BY)) {
      sql.VALUES(TABLE_COLUMN_NAME_CREATED_BY, "'" + AuthenticationUtils.getId() + "'");
    }
    if (fieldNames.contains(VARIABLE_NAME_UPDATED_BY)) {
      sql.VALUES(TABLE_COLUMN_NAME_UPDATED_BY, "'" + AuthenticationUtils.getId() + "'");
    }

    log.debug(sql.toString());
    return sql.toString();
  }

  public <T> String insertBatch(@NonNull final List<T> entities) {
    if (NullUtils.isEmpty(entities)) {
      log.warn("entities size() : 0");
      throw BusinessException.ERROR_SYSTEM;
    }
    final SQL sql = new SQL();
    sql.INSERT_INTO(getTableName(entities.get(0).getClass().getSimpleName()));
    final List<String> columns = this.getEntityFields(entities.get(0));

    final String intoColumns =
        StringUtils.join(
            columns.stream()
                .map(CaseUtils::getCamelCaseToSnakeCase)
                .collect(Collectors.joining(",")));
    sql.INTO_COLUMNS(intoColumns);

    final List<List<Object>> valuesList = new ArrayList<>();
    entities.stream()
        .map(MapperUtils::toMap)
        .forEach(
            entity -> {
              final List<Object> values = new ArrayList<>();
              columns.forEach(
                  column -> {
                    if (StringUtils.equalsAny(
                        column, VARIABLE_NAME_CREATED, VARIABLE_NAME_UPDATED)) {
                      values.add(SYSDATE);
                    } else if (StringUtils.equalsAny(
                        column, VARIABLE_NAME_CREATED_BY, VARIABLE_NAME_UPDATED_BY)) {
                      values.add("'" + AuthenticationUtils.getId() + "'");
                    } else {
                      final Object o = entity.get(column);
                      values.add(
                          o == null
                              ? "null"
                              : o instanceof String ? MessageFormat.format("''{0}''", o) : o);
                    }
                  });
              if (!values.contains(VARIABLE_NAME_CREATED)
                  && !values.contains(VARIABLE_NAME_UPDATED)
                  && !values.contains(VARIABLE_NAME_CREATED_BY)
                  && !values.contains(VARIABLE_NAME_UPDATED_BY)) {
                if (StringUtils.containsAny(
                    intoColumns, VARIABLE_NAME_CREATED, VARIABLE_NAME_UPDATED)) {
                  values.add(SYSDATE);
                } else if (StringUtils.containsAny(
                    intoColumns, VARIABLE_NAME_CREATED_BY, VARIABLE_NAME_UPDATED_BY)) {
                  values.add("'" + AuthenticationUtils.getId() + "'");
                }
              }
              valuesList.add(values);
            });
    sql.INTO_VALUES(
        valuesList.stream()
            .map(value -> StringUtils.join(value, ","))
            .collect(Collectors.joining("), (")));
    log.debug(sql.toString());
    return sql.toString();
  }

  public <T> String updateById(final T entity, final Long id) {
    return this.updateByMap(entity, Map.of("id", id));
  }

  public <T> String updateByMap(final T entity, final Map<String, Object> whereConditions) {
    this.verifyWhereKey(whereConditions);

    final SQL sql = new SQL();
    sql.UPDATE(getTableName(entity.getClass().getSimpleName()));
    final Map<String, Object> param = MapperUtils.toMap(entity);
    this.getEntityFields(entity).stream()
        .filter(
            fieldName ->
                !StringUtils.equalsAny(
                    fieldName,
                    VARIABLE_NAME_CREATED_BY,
                    VARIABLE_NAME_CREATED,
                    VARIABLE_NAME_UPDATED,
                    VARIABLE_NAME_UPDATED_BY))
        .filter(fieldName -> !whereConditions.containsKey(fieldName))
        .forEach(
            javaFieldName ->
                sql.SET(
                    MessageFormat.format(
                        SET_STRING,
                        CaseUtils.getCamelCaseToSnakeCase(javaFieldName),
                        javaFieldName,
                        this.getJdbcType(param.get(javaFieldName)),
                        1)));

    whereConditions.forEach(
        (key, value) ->
            sql.WHERE(
                MessageFormat.format(
                    WHERE_EQUALS_STRING,
                    CaseUtils.getCamelCaseToSnakeCase(key),
                    key,
                    this.getJdbcType(value),
                    2)));

    this.getUpdateSetSql(sql, this.getEntityFields(entity));
    if (!StringUtils.containsIgnoreCase(sql.toString(), "WHERE ")) {
      log.warn("whereConditions is empty");
      throw BusinessException.ERROR_SYSTEM;
    }
    log.debug(sql.toString());
    return sql.toString();
  }

  public String updateMapByMap(
      final Map<String, Object> updateMap, final Map<String, Object> whereConditions) {
    this.verifyWhereKey(whereConditions);

    final SQL sql = new SQL();
    sql.UPDATE(this.getTableName());
    updateMap.forEach(
        (javaFieldName, value) ->
            sql.SET(
                MessageFormat.format(
                    SET_STRING,
                    CaseUtils.getCamelCaseToSnakeCase(javaFieldName),
                    javaFieldName,
                    this.getJdbcType(value),
                    1)));

    whereConditions.forEach(
        (key, value) ->
            sql.WHERE(
                MessageFormat.format(
                    WHERE_EQUALS_STRING,
                    CaseUtils.getCamelCaseToSnakeCase(key),
                    key,
                    this.getJdbcType(value),
                    2)));

    this.getUpdateSetSql(sql, this.getEntityFields());
    if (!StringUtils.containsIgnoreCase(sql.toString(), "WHERE ")) {
      log.warn("whereConditions is empty");
      throw BusinessException.ERROR_SYSTEM;
    }
    log.debug(sql.toString());
    return sql.toString();
  }

  private void getUpdateSetSql(final SQL sql, final List<String> fieldNames) {
    if (fieldNames.contains(VARIABLE_NAME_UPDATED)) {
      sql.SET(TABLE_COLUMN_NAME_UPDATED + " = " + SYSDATE);
    }
    if (fieldNames.contains(VARIABLE_NAME_UPDATED_BY)) {
      sql.SET(
          MessageFormat.format(
              SET_UPDATED_BY_STRING, TABLE_COLUMN_NAME_UPDATED_BY, AuthenticationUtils.getId()));
    }
  }

  public String deleteById(final Long id) {
    return this.deleteByMap(Map.of("id", id));
  }

  public String deleteByMap(final Map<String, Object> whereConditions) {
    this.verifyWhereKey(whereConditions);
    final SQL sql = new SQL();
    sql.DELETE_FROM(this.getTableName());
    this.getWhereSql(sql, whereConditions);
    this.requiredWhereConditions(sql);
    log.debug(sql.toString());
    return sql.toString();
  }

  public String countForDataTable(final DataTableFilterDTO dataTableFilterDTO) {
    final SQL sql = new SQL();
    sql.SELECT("COUNT(1) AS CNT").FROM(this.getTableName());
    Optional.ofNullable(dataTableFilterDTO.getFilter())
        .ifPresent(item -> this.getWhereBoundSql(sql, item));
    log.debug(sql.toString());
    return sql.toString();
  }

  public String countForSearchAndDataTable(
      final String search,
      final List<String> targetColumns,
      final DataTableFilterDTO dataTableFilterDTO) {
    for (final String targetColumn : targetColumns) {
      dataTableFilterDTO.getFilter().put(MessageFormat.format("{}:contains", targetColumn), search);
    }
    return this.countForDataTable(dataTableFilterDTO);
  }

  public String getItemsForDataTable(final DataTableFilterDTO dataTableFilterDTO) {
    final SQL sql = new SQL();
    this.getSelectSql(sql, this.getEntityFields());
    sql.FROM(this.getTableName());

    Optional.ofNullable(dataTableFilterDTO.getFilter())
        .ifPresent(item -> this.getWhereBoundSql(sql, item));

    Optional.ofNullable(dataTableFilterDTO.getSortBy())
        .ifPresent(
            items ->
                items.forEach(
                    columns ->
                        sql.ORDER_BY(
                            columns.startsWith("-")
                                ? CaseUtils.getCamelCaseToSnakeCase(columns.replaceFirst("-", ""))
                                    + " DESC"
                                : CaseUtils.getCamelCaseToSnakeCase(columns) + " ASC")));
    if (dataTableFilterDTO.getPage() != 0) {
      sql.LIMIT(dataTableFilterDTO.getItemsPerPage());
      sql.OFFSET(dataTableFilterDTO.getStartIndex());
    }
    log.debug(sql.toString());
    return sql.toString();
  }

  public String getItemsForSearchAndDataTable(
      final String search,
      final List<String> targetColumns,
      final DataTableFilterDTO dataTableFilterDTO) {
    for (final String targetColumn : targetColumns) {
      dataTableFilterDTO.getFilter().put(MessageFormat.format("{}:contains", targetColumn), search);
    }
    return this.getItemsForDataTable(dataTableFilterDTO);
  }

  private void requiredWhereConditions(final SQL sql) {
    if (!StringUtils.containsIgnoreCase(sql.toString(), "WHERE ")) {
      log.warn("whereConditions is empty");
      throw BusinessException.ERROR_SYSTEM;
    }
  }

  private String getJdbcType(final Object object) {
    final String jdbcType;
    if (object instanceof String || object instanceof Character) {
      jdbcType = ", jdbcType=VARCHAR";
    } else if (object instanceof Short) {
      jdbcType = ", jdbcType=SMALLINT";
    } else if (object instanceof Integer) {
      jdbcType = ", jdbcType=INTEGER";
    } else if (object instanceof Long) {
      jdbcType = ", jdbcType=BIGINT";
    } else if (object instanceof Double) {
      jdbcType = ", jdbcType=DOUBLE";
    } else if (object instanceof Instant) {
      jdbcType = ", jdbcType=TIMESTAMP";
    } else if (object instanceof Boolean) {
      jdbcType = ", jdbcType=BOOLEAN";
    } else if (object instanceof Byte[]) {
      jdbcType = ", jdbcType=BLOB";
    } else if (object == null) {
      jdbcType = ", jdbcType=NULL";
    } else {
      jdbcType = "";
      log.warn("케이스 빠짐 {}", object.getClass().getSimpleName());
    }
    return jdbcType;
  }

  private void getWhereBoundSql(final SQL sql, final Map<String, Object> whereConditions) {
    whereConditions.forEach(
        (key, value) -> {
          final String columnName = StringUtils.substringBefore(key, ":");
          final String conditionType =
              StringUtils.defaultString(StringUtils.substringAfter(key, ":"), "equals");
          if (conditionType.equals("set")) {
            sql.WHERE(
                MessageFormat.format(
                    WHERE_IN_STRING,
                    CaseUtils.getCamelCaseToSnakeCase(columnName),
                    StringUtils.defaultIfEmpty(
                        MapperUtils.toArrayList(value, String.class).stream()
                            .map(item -> "'" + item + "'")
                            .collect(Collectors.joining(",")),
                        "''")));
          } else if (conditionType.equals("notSet")) {
            sql.WHERE(
                MessageFormat.format(
                    WHERE_NOT_IN_STRING,
                    CaseUtils.getCamelCaseToSnakeCase(columnName),
                    StringUtils.defaultIfEmpty(
                        MapperUtils.toArrayList(value, String.class).stream()
                            .map(item -> "'" + item + "'")
                            .collect(Collectors.joining(",")),
                        "''")));
          } else {
            String whereString = this.getWhereString(conditionType);
            whereString = whereString.replace("whereConditions.", "");
            whereString = whereString.replace("{0}", CaseUtils.getCamelCaseToSnakeCase(columnName));
            if (StringUtils.countMatches(((String) value), '-') == 2
                && StringUtils.countMatches(((String) value), ':') == 2
                && StringUtils.countMatches(((String) value), 'T') == 1
                && StringUtils.endsWith(((String) value), "Z")) {
              whereString =
                  whereString.replace(
                      "#'{'{1}{2}'}'",
                      "FROM_UNIXTIME("
                          + Integer.parseInt(
                              String.valueOf(Instant.parse((String) value).toEpochMilli() / 1000))
                          + ")");
            } else {
              if (conditionType.startsWith("boolean")) {
                whereString = whereString.replace("#'{'{1}{2}'}'", (String) value);
              } else {
                whereString = whereString.replace("#'{'{1}{2}'}'", "'" + value + "'");
              }
            }
            sql.WHERE(whereString);
          }
        });
  }
}
