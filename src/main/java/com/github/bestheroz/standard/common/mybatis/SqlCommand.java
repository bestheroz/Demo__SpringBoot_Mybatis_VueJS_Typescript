package com.github.bestheroz.standard.common.mybatis;

import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import com.github.bestheroz.standard.common.util.CaseUtils;
import com.github.bestheroz.standard.common.util.DateUtils;
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
  public static final String SELECT_ITEMS_BY_MAP_WITH_ORDER = "getItemsByMapWithOrder";
  public static final String SELECT_TARGET_ITEMS_BY_MAP_WITH_ORDER = "getTargetItemsByMapWithOrder";
  public static final String SELECT_ITEMS_BY_DATATABLE = "getItemsForDataTable";
  public static final String SELECT_ITEM_BY_MAP = "getItemByMap";
  public static final String COUNT_BY_DATATABLE = "countForDataTable";
  public static final String COUNT_BY_MAP = "countByMap";
  public static final String INSERT = "insert";
  public static final String INSERT_BATCH = "insertBatch";
  public static final String UPDATE_MAP_BY_MAP = "updateMapByMap";
  public static final String DELETE_BY_MAP = "deleteByMap";
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
  private static final Set<String> METHOD_LIST =
      Set.of(
          SELECT_ITEMS_BY_MAP_WITH_ORDER,
          SELECT_TARGET_ITEMS_BY_MAP_WITH_ORDER,
          SELECT_ITEMS_BY_DATATABLE,
          SELECT_ITEM_BY_MAP,
          COUNT_BY_MAP,
          COUNT_BY_DATATABLE,
          INSERT,
          UPDATE_MAP_BY_MAP,
          DELETE_BY_MAP);

  public String getTableName() {
    return getTableName(this.getEntityClass().getSimpleName());
  }

  public static String getTableName(final String javaClassName) {
    return StringUtils.lowerCase(CaseUtils.getCamelCaseToSnakeCase(javaClassName));
  }

  private void getSelectSql(final SQL sql, final Set<String> columns) {
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

  public String countByMap(final Map<String, Object> whereConditions) {
    final SQL sql = new SQL();
    sql.SELECT("COUNT(1) AS CNT").FROM(this.getTableName());
    this.getWhereSql(sql, whereConditions);
    log.debug(sql.toString());
    return sql.toString();
  }

  private Class<?> getEntityClass() {
    final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
    final Optional<StackTraceElement> stackTraceElements =
        Arrays.stream(stackTrace)
            .filter(
                item -> {
                  try {
                    return (METHOD_LIST.contains(item.getMethodName())
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
    if (stackTraceElements.isEmpty()) {
      log.warn("stackTraceElements is Empty()");
      throw BusinessException.ERROR_SYSTEM;
    }
    final StackTraceElement item = stackTraceElements.get();
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

  public String getItemsByMapWithOrder(
      final Map<String, Object> whereConditions, final List<String> orderByConditions) {
    return this.select(whereConditions, orderByConditions);
  }

  public String getTargetItemsByMapWithOrder(
      final Set<String> targetColumns,
      final Map<String, Object> whereConditions,
      final List<String> orderByConditions) {
    return this.select(targetColumns, whereConditions, orderByConditions);
  }

  private String select(
      final Map<String, Object> whereConditions, final List<String> orderByConditions) {
    return this.select(this.getEntityFields(), whereConditions, orderByConditions);
  }

  // ordered list required
  private <T> Set<String> getEntityFields(final T entity) {
    return Stream.concat(
            Arrays.stream(entity.getClass().getDeclaredFields()).map(Field::getName),
            Arrays.stream(entity.getClass().getSuperclass().getDeclaredFields())
                .map(Field::getName))
        .distinct()
        .filter(fieldName -> !EXCLUDE_FIELD_SET.contains(fieldName))
        .collect(Collectors.toSet());
  }

  private Set<String> getEntityFields() {
    final Class<?> tClass = this.getEntityClass();
    return Stream.concat(
            Arrays.stream(tClass.getDeclaredFields()).map(Field::getName),
            Arrays.stream(tClass.getSuperclass().getDeclaredFields()).map(Field::getName))
        .distinct()
        .filter(fieldName -> !EXCLUDE_FIELD_SET.contains(fieldName))
        .collect(Collectors.toSet());
  }

  private String select(
      final Set<String> targetColumns,
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

  public <T> String insert(@NonNull final T entity) {
    final SQL sql = new SQL();
    sql.INSERT_INTO(getTableName(entity.getClass().getSimpleName()));
    MapperUtils.toMap(entity).entrySet().stream()
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
                    this.getFormattedValue(item.getValue())));

    final Set<String> fieldNames = this.getEntityFields(entity);

    if (fieldNames.contains(VARIABLE_NAME_CREATED)) {
      sql.VALUES(TABLE_COLUMN_NAME_CREATED, SYSDATE);
    }
    if (fieldNames.contains(VARIABLE_NAME_UPDATED)) {
      sql.VALUES(TABLE_COLUMN_NAME_UPDATED, SYSDATE);
    }
    if (fieldNames.contains(VARIABLE_NAME_CREATED_BY)) {
      sql.VALUES(TABLE_COLUMN_NAME_CREATED_BY, AuthenticationUtils.getId().toString());
    }
    if (fieldNames.contains(VARIABLE_NAME_UPDATED_BY)) {
      sql.VALUES(TABLE_COLUMN_NAME_UPDATED_BY, AuthenticationUtils.getId().toString());
    }

    log.debug(sql.toString());
    return sql.toString();
  }

  public <T> String insertBatch(@NonNull final List<T> entities) {
    if (NullUtils.isEmpty(entities)) {
      log.warn("entities empty");
      throw BusinessException.ERROR_SYSTEM;
    }
    final SQL sql = new SQL();
    sql.INSERT_INTO(getTableName(entities.get(0).getClass().getSimpleName()));
    final Set<String> columns = this.getEntityFields(entities.get(0));

    sql.INTO_COLUMNS(
        columns.stream().map(CaseUtils::getCamelCaseToSnakeCase).collect(Collectors.joining(",")));

    final List<List<String>> valuesList = new ArrayList<>();
    entities.stream()
        .map(MapperUtils::toMap)
        .forEach(
            entity -> {
              final List<String> values =
                  columns.stream()
                      .map(
                          column -> {
                            if (StringUtils.equalsAny(
                                column, VARIABLE_NAME_CREATED, VARIABLE_NAME_UPDATED)) {
                              return SYSDATE;
                            } else if (StringUtils.equalsAny(
                                column, VARIABLE_NAME_CREATED_BY, VARIABLE_NAME_UPDATED_BY)) {
                              return AuthenticationUtils.getId().toString();
                            } else {
                              return this.getFormattedValue(entity.get(column));
                            }
                          })
                      .toList();
              if (!values.contains(VARIABLE_NAME_CREATED)
                  && !values.contains(VARIABLE_NAME_UPDATED)
                  && !values.contains(VARIABLE_NAME_CREATED_BY)
                  && !values.contains(VARIABLE_NAME_UPDATED_BY)) {
                if (StringUtils.containsAny(
                    columns.stream()
                        .map(CaseUtils::getCamelCaseToSnakeCase)
                        .collect(Collectors.joining(",")),
                    VARIABLE_NAME_CREATED,
                    VARIABLE_NAME_UPDATED)) {
                  values.add(SYSDATE);
                } else if (StringUtils.containsAny(
                    columns.stream()
                        .map(CaseUtils::getCamelCaseToSnakeCase)
                        .collect(Collectors.joining(",")),
                    VARIABLE_NAME_CREATED_BY,
                    VARIABLE_NAME_UPDATED_BY)) {
                  values.add(AuthenticationUtils.getId().toString());
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

  public String updateMapByMap(
      final Map<String, Object> updateMap, final Map<String, Object> whereConditions) {
    this.verifyWhereKey(whereConditions);

    final SQL sql = new SQL();
    sql.UPDATE(this.getTableName());
    updateMap.forEach(
        (javaFieldName, value) -> {
          if (StringUtils.equalsAny(
              javaFieldName,
              VARIABLE_NAME_CREATED_BY,
              VARIABLE_NAME_CREATED,
              VARIABLE_NAME_UPDATED,
              VARIABLE_NAME_UPDATED_BY)) {
            return;
          }
          sql.SET(this.getEqualSql(CaseUtils.getCamelCaseToSnakeCase(javaFieldName), value));
        });

    this.getWhereSql(sql, whereConditions);
    this.getUpdateSetSql(sql, this.getEntityFields());
    if (!StringUtils.containsIgnoreCase(sql.toString(), "WHERE ")) {
      log.warn("whereConditions is empty");
      throw BusinessException.ERROR_SYSTEM;
    }
    // jdbcType=JSON 보정
    log.debug(sql.toString());
    return sql.toString();
  }

  private void getUpdateSetSql(final SQL sql, final Set<String> fieldNames) {
    if (fieldNames.contains(VARIABLE_NAME_UPDATED)) {
      sql.SET(TABLE_COLUMN_NAME_UPDATED + " = " + SYSDATE);
    }
    if (fieldNames.contains(VARIABLE_NAME_UPDATED_BY)) {
      sql.SET(
          MessageFormat.format(
              "{0} = ''{1}''", TABLE_COLUMN_NAME_UPDATED_BY, AuthenticationUtils.getId()));
    }
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
    if (StringUtils.isNotEmpty(dataTableFilterDTO.getSearch())) {
      for (final String searchColumn : dataTableFilterDTO.getSearchColumns()) {
        dataTableFilterDTO
            .getFilter()
            .put(
                MessageFormat.format("{0}:contains", searchColumn), dataTableFilterDTO.getSearch());
      }
    }

    final SQL sql = new SQL();
    sql.SELECT("COUNT(1) AS CNT").FROM(this.getTableName());
    Optional.ofNullable(dataTableFilterDTO.getFilter())
        .ifPresent(item -> this.getWhereSql(sql, item));
    log.debug(sql.toString());
    return sql.toString();
  }

  public String getItemsForDataTable(final DataTableFilterDTO dataTableFilterDTO) {
    if (StringUtils.isNotEmpty(dataTableFilterDTO.getSearch())) {
      for (final String searchColumn : dataTableFilterDTO.getSearchColumns()) {
        dataTableFilterDTO
            .getFilter()
            .put(
                MessageFormat.format("{0}:contains", searchColumn), dataTableFilterDTO.getSearch());
      }
    }

    final SQL sql = new SQL();
    this.getSelectSql(sql, this.getEntityFields());
    sql.FROM(this.getTableName());

    Optional.ofNullable(dataTableFilterDTO.getFilter())
        .ifPresent(item -> this.getWhereSql(sql, item));

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

  private void requiredWhereConditions(final SQL sql) {
    if (!StringUtils.containsIgnoreCase(sql.toString(), "WHERE ")) {
      log.warn("whereConditions is empty");
      throw BusinessException.ERROR_SYSTEM;
    }
  }

  private String getWhereString(
      final String conditionType, final String dbColumnName, final Object value) {
    switch (conditionType) {
      case "eq":
      default:
        return this.getEqualSql(dbColumnName, value);
      case "ne":
        return MessageFormat.format("{0} <> {1}", dbColumnName, this.getFormattedValue(value));
      case "in":
        {
          final Set<?> values = (Set<?>) value;
          if (values.isEmpty()) {
            log.warn("WHERE - empty in cause : {}", dbColumnName);
            throw new BusinessException(ExceptionCode.FAIL_NO_DATA_SUCCESS);
          }
          return MessageFormat.format(
              "{0} IN ({1})",
              dbColumnName,
              values.stream().map(this::getFormattedValue).collect(Collectors.joining(",")));
        }
      case "notIn":
        final Set<?> values = (Set<?>) value;
        if (values.isEmpty()) {
          log.warn("WHERE - empty in cause : {}", dbColumnName);
          throw new BusinessException(ExceptionCode.FAIL_NO_DATA_SUCCESS);
        }
        return MessageFormat.format(
            "{0} NOT IN ({1})",
            dbColumnName,
            values.stream().map(this::getFormattedValue).collect(Collectors.joining(",")));
      case "null":
        return MessageFormat.format("{0} is null", dbColumnName);
      case "notNull":
        return MessageFormat.format("{0} is not null", dbColumnName);
      case "contains":
        return MessageFormat.format(
            "INSTR({0}, {1}) > 0", dbColumnName, this.getFormattedValue(value));
      case "notContains":
        return MessageFormat.format(
            "INSTR({0}, {1}) = 0", dbColumnName, this.getFormattedValue(value));
      case "startsWith":
        return MessageFormat.format(
            "INSTR({0}, {1}) = 1", dbColumnName, this.getFormattedValue(value));
      case "endsWith":
        return MessageFormat.format(
            "RIGHT({0}, CHAR_LENGTH({1})) = {1}", dbColumnName, this.getFormattedValue(value));
      case "lt":
        return MessageFormat.format("{0} < {1}", dbColumnName, this.getFormattedValue(value));
      case "lte":
        return MessageFormat.format("{0} <= {1}", dbColumnName, this.getFormattedValue(value));
      case "gt":
        return MessageFormat.format("{0} > {1}", dbColumnName, this.getFormattedValue(value));
      case "gte":
        return MessageFormat.format("{0} >= {1}", dbColumnName, this.getFormattedValue(value));
    }
  }

  private String getEqualSql(final String dbColumnName, final Object value) {
    return MessageFormat.format("{0} = {1}", dbColumnName, this.getFormattedValue(value));
  }

  private String getFormattedValue(final Object value) {
    if (value == null) {
      return "null";
    } else if (value instanceof String str) {
      if (this.isISO8601String(str)) {
        return "'" + DateUtils.toString(Instant.parse(str), "yyyy-MM-dd HH:mm:ss.SSS") + "'";
        // MYSQL
        //        return MessageFormat.format(
        //            "FROM_UNIXTIME({0,number,#})",
        //            Integer.parseInt(String.valueOf(Instant.parse(str).toEpochMilli() / 1000)));
      } else {
        return "'" + value + "'";
      }
    } else if (value instanceof Set || value instanceof List) {
      return "'" + MapperUtils.toJsonArray(value).toString() + "'";
    } else if (value instanceof Instant instant) {
      return MessageFormat.format(
          "FROM_UNIXTIME({0,number,#})",
          Integer.parseInt(String.valueOf((instant).toEpochMilli() / 1000)));
    } else {
      return value.toString();
    }
  }

  private void getWhereSql(final SQL sql, final Map<String, Object> whereConditions) {
    whereConditions.forEach(
        (key, value) -> {
          final String columnName = StringUtils.substringBefore(key, ":");
          final String conditionType =
              StringUtils.defaultString(StringUtils.substringAfter(key, ":"), "eq");
          sql.WHERE(
              this.getWhereString(
                  conditionType, CaseUtils.getCamelCaseToSnakeCase(columnName), value));
        });
  }

  private boolean isISO8601String(final String value) {
    return StringUtils.countMatches(value, '-') == 2
        && StringUtils.countMatches(value, ':') == 2
        && StringUtils.countMatches(value, 'T') == 1
        && StringUtils.endsWith(value, "Z");
  }
}
