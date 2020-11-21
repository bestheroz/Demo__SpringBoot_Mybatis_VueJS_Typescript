package com.github.bestheroz.standard.common.mybatis;

import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import com.github.bestheroz.standard.common.util.MapperUtils;
import com.github.bestheroz.standard.common.util.NullUtils;
import com.google.common.base.CaseFormat;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.apache.tika.utils.ExceptionUtils;
import org.springframework.lang.NonNull;

@Slf4j
public class SqlCommand {

  public static final String SELECT_ITEMS = "getItems";
  public static final String SELECT_ITEMS_WITH_ORDER = "getItemsWithOrder";
  public static final String SELECT_ITEMS_BY_KEY = "getItemsByKey";
  public static final String SELECT_ITEMS_BY_KEY_WITH_ORDER =
    "getItemsByKeyWithOrder";
  public static final String SELECT_ITEM_BY_KEY = "getItemByKey";
  public static final String COUNT_ALL = "countAll";
  public static final String COUNT_BY_KEY = "countByKey";
  public static final String INSERT = "insert";
  public static final String UPDATE_BY_KEY = "updateByKey";
  public static final String UPDATE_MAP_BY_KEY = "updateMapByKey";
  public static final String DELETE_BY_KEY = "deleteByKey";
  public static final Set<String> VARCHAR_JAVA_TYPE_SET = Set.of(
    "String",
    "Char"
  );
  public static final Set<String> SHORT_JAVA_TYPE_SET = Set.of("Short");
  public static final Set<String> INTEGER_JAVA_TYPE_SET = Set.of("Integer");
  public static final Set<String> BIGINT_JAVA_TYPE_SET = Set.of("Long");
  public static final Set<String> NUMBER_JAVA_TYPE_SET = Set.of(
    "Short",
    "Integer",
    "Long",
    "Double"
  );
  public static final Set<String> DOUBLE_JAVA_TYPE_SET = Set.of("Double");
  public static final Set<String> TIMESTAMP_JAVA_TYPE_SET = Set.of("Instant");
  public static final Set<String> BLOB_JAVA_TYPE_SET = Set.of("Byte[]");
  public static final Set<String> BOOLEAN_JAVA_TYPE_SET = Set.of(
    "Boolean",
    "boolean"
  );
  // 참고용: 각VO에 암호화 컬럼 정의 방법
  private static final Set<String> ENCRYPTED_COLUMN_LIST = Set.of(
    "empnm",
    "smsphone"
  );
  private static final String TABLE_COLUMN_NAME_CREATED_BY = "CREATED_BY";
  private static final String TABLE_COLUMN_NAME_CREATED = "CREATED";
  private static final String TABLE_COLUMN_NAME_UPDATED_BY = "UPDATED_BY";
  private static final String TABLE_COLUMN_NAME_UPDATED = "UPDATED";
  private static final String VARIABLE_NAME_CREATED_BY = "createdBy";
  private static final String VARIABLE_NAME_CREATED = "created";
  private static final String VARIABLE_NAME_UPDATED_BY = "updatedBy";
  private static final String VARIABLE_NAME_UPDATED = "updated";
  private static final String SYSDATE = "NOW()";
  public static final Set<String> EXCLUDE_FIELD_SET = Set.of(
    "SERIAL_VERSION_U_I_D",
    "serialVersionUID",
    "E_N_C_R_Y_P_T_E_D__C_O_L_U_M_N__L_I_S_T"
  );
  private static final String SELECT_ENCRYPTED_STRING =
    "FNC_GET_DECRYPT ({0}) AS {0}";
  private static final String INSERT_BIND_STRING = "#'{'{0}{1}'}'";
  private static final String INSERT_BIND_ENCRYPTED_STRING =
    "FNC_GET_ENCRYPT (#'{'{1}{2}'}')";
  private static final String SET_BIND_STRING = "{0} = #'{'param{3}.{1}{2}'}'";
  private static final String SET_UPDATED_BY_BIND_STRING = "{0} = ''{1}''";
  private static final String SET_BIND_ENCRYPTED_STRING =
    "{0} = FNC_GET_ENCRYPT (#'{'param{3}.{1}{2}'}')";
  private static final String WHERE_BIND_STRING =
    "{0} = #'{'param{3}.{1}{2}'}'";
  private static final String WHERE_BIND_ENCRYPTED_STRING =
    "{0} = FNC_GET_ENCRYPT (#'{'param{3}.{1}{2}'}')";

  public static String getTableName(final String javaClassName) {
    return CaseFormat.LOWER_CAMEL.to(
      CaseFormat.UPPER_UNDERSCORE,
      StringUtils.substringBetween(javaClassName, "Table", "Entity")
    );
  }

  private void getSelectSql(final SQL sql, final Field[] fields) {
    Arrays
      .stream(fields)
      .map(Field::getName)
      .distinct()
      .filter(fieldName -> !EXCLUDE_FIELD_SET.contains(fieldName))
      .forEach(
        fieldName -> {
          final String dbColumnName = this.getCamelCaseToSnakeCase(fieldName);
          if (ENCRYPTED_COLUMN_LIST.contains(fieldName)) {
            sql.SELECT(
              MessageFormat.format(SELECT_ENCRYPTED_STRING, dbColumnName)
            );
          } else {
            sql.SELECT(dbColumnName);
          }
        }
      );
  }

  private void verifyWhereKey(final Map<String, Object> whereConditions) {
    if (NullUtils.size(whereConditions) < 1) {
      log.warn(ExceptionCode.FAIL_NO_DATA_SUCCESS.toString());
      throw BusinessException.FAIL_NO_DATA_SUCCESS;
    }
  }

  @SuppressWarnings("SameParameterValue")
  private void getWhereSql(
    final SQL sql,
    final Map<String, Object> whereConditions,
    final int whereEntityPosition
  ) {
    whereConditions.forEach(
      (key, value) -> {
        if (ENCRYPTED_COLUMN_LIST.contains(key)) {
          sql.WHERE(
            MessageFormat.format(
              whereEntityPosition == 0
                ? WHERE_BIND_ENCRYPTED_STRING.replace("param{3}.", "")
                : WHERE_BIND_ENCRYPTED_STRING,
              this.getCamelCaseToSnakeCase(key),
              key,
              this.getJdbcType(value),
              whereEntityPosition
            )
          );
        } else {
          sql.WHERE(
            MessageFormat.format(
              whereEntityPosition == 0
                ? WHERE_BIND_STRING.replace("param{3}.", "")
                : WHERE_BIND_STRING,
              this.getCamelCaseToSnakeCase(key),
              key,
              this.getJdbcType(value),
              whereEntityPosition
            )
          );
        }
      }
    );
  }

  public String countAll() {
    return this.countByKey(Map.of());
  }

  public String countByKey(final Map<String, Object> whereConditions) {
    final SQL sql = new SQL();
    sql
      .SELECT("COUNT(1) AS CNT")
      .FROM(getTableName(this.getEntityClass().getSimpleName()));
    this.getWhereSql(sql, whereConditions, 0);
    log.debug(sql.toString());
    return sql.toString();
  }

  public String getItems() {
    return this.select(Map.of(), Set.of(), 0);
  }

  private Class<?> getEntityClass() {
    final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
    final Optional<StackTraceElement> getItems = Arrays
      .stream(stackTrace)
      .filter(
        item -> {
          try {
            return (
              item.getClassName().startsWith("com.sun.proxy.$Proxy") &&
              List
                .of(
                  SELECT_ITEMS,
                  SELECT_ITEMS_WITH_ORDER,
                  SELECT_ITEMS_BY_KEY,
                  SELECT_ITEMS_BY_KEY_WITH_ORDER,
                  SELECT_ITEM_BY_KEY,
                  COUNT_ALL,
                  COUNT_BY_KEY,
                  INSERT,
                  UPDATE_BY_KEY,
                  UPDATE_MAP_BY_KEY,
                  DELETE_BY_KEY
                )
                .contains(item.getMethodName()) &&
              Class.forName(item.getClassName()).getInterfaces().length > 0 &&
              Class
                .forName(item.getClassName())
                .getInterfaces()[0].getGenericInterfaces()
                .length >
              0
            );
          } catch (final ClassNotFoundException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw BusinessException.ERROR_SYSTEM;
          }
        }
      )
      .findFirst();
    getItems.orElseThrow(
      () -> {
        throw BusinessException.ERROR_SYSTEM;
      }
    );
    final StackTraceElement item = getItems.get();
    try {
      final Class<?> cInterface = Class
        .forName(item.getClassName())
        .getInterfaces()[0];
      return Class.forName(
        StringUtils.substringBetween(
          cInterface.getGenericInterfaces()[0].getTypeName(),
          "<",
          ">"
        )
      );
    } catch (final ClassNotFoundException e) {
      log.warn(ExceptionUtils.getStackTrace(e));
      throw BusinessException.ERROR_SYSTEM;
    }
  }

  public String getItemsWithOrder(final Set<String> orderByConditions) {
    return this.select(Map.of(), orderByConditions, 0);
  }

  public String getItemsByKey(final Map<String, Object> whereConditions) {
    return this.select(whereConditions, Set.of(), 0);
  }

  public String getItemsByKeyWithOrder(
    final Map<String, Object> whereConditions,
    final Set<String> orderByConditions
  ) {
    return this.select(whereConditions, orderByConditions, 1);
  }

  public String select(
    final Map<String, Object> whereConditions,
    final Set<String> orderByConditions,
    final Integer whereEntityPosition
  ) {
    final SQL sql = new SQL();
    final Class<?> tClass = this.getEntityClass();
    this.getSelectSql(
        sql,
        ArrayUtils.addAll(
          tClass.getDeclaredFields(),
          tClass.getSuperclass().getDeclaredFields()
        )
      );
    sql.FROM(getTableName(tClass.getSimpleName()));
    this.getWhereSql(sql, whereConditions, whereEntityPosition);
    orderByConditions.forEach(
      columns -> sql.ORDER_BY(this.getCamelCaseToSnakeCase(columns))
    );
    log.debug(sql.toString());
    return sql.toString();
  }

  public String getItemByKey(
    @NonNull final Map<String, Object> whereConditions
  ) {
    this.verifyWhereKey(whereConditions);
    return this.select(whereConditions, Set.of(), 0);
  }

  public <T> String insert(@NonNull final T entity) {
    final Map<String, Object> param = MapperUtils.toHashMap(entity);
    final SQL sql = new SQL();
    sql.INSERT_INTO(getTableName(entity.getClass().getSimpleName()));
    param
      .entrySet()
      .stream()
      .filter(
        item ->
          !StringUtils.equalsAny(
            item.getKey(),
            VARIABLE_NAME_CREATED,
            VARIABLE_NAME_CREATED_BY,
            VARIABLE_NAME_UPDATED,
            VARIABLE_NAME_UPDATED_BY
          )
      )
      .forEach(
        item -> {
          final String javaFieldName = item.getKey();
          final String dbColumnName =
            this.getCamelCaseToSnakeCase(javaFieldName);
          if (ENCRYPTED_COLUMN_LIST.contains(javaFieldName)) {
            sql.VALUES(
              dbColumnName,
              MessageFormat.format(
                INSERT_BIND_ENCRYPTED_STRING,
                dbColumnName,
                javaFieldName,
                this.getJdbcType(item.getValue())
              )
            );
          } else {
            sql.VALUES(
              dbColumnName,
              MessageFormat.format(
                INSERT_BIND_STRING,
                javaFieldName,
                this.getJdbcType(item.getValue())
              )
            );
          }
        }
      );

    final Set<String> fieldNames = Arrays
      .stream(
        ArrayUtils.addAll(
          entity.getClass().getSuperclass().getDeclaredFields(),
          entity.getClass().getDeclaredFields()
        )
      )
      .map(Field::getName)
      .collect(Collectors.toSet());

    if (fieldNames.contains(VARIABLE_NAME_CREATED)) {
      sql.VALUES(TABLE_COLUMN_NAME_CREATED, SYSDATE);
    }
    if (fieldNames.contains(VARIABLE_NAME_UPDATED)) {
      sql.VALUES(TABLE_COLUMN_NAME_UPDATED, SYSDATE);
    }
    if (fieldNames.contains(VARIABLE_NAME_CREATED_BY)) {
      sql.VALUES(
        TABLE_COLUMN_NAME_CREATED_BY,
        "'" + AuthenticationUtils.getUserPk() + "'"
      );
    }
    if (fieldNames.contains(VARIABLE_NAME_UPDATED_BY)) {
      sql.VALUES(
        TABLE_COLUMN_NAME_UPDATED_BY,
        "'" + AuthenticationUtils.getUserPk() + "'"
      );
    }

    log.debug(sql.toString());
    return sql.toString();
  }

  public <T> String updateByKey(
    final T entity,
    final Map<String, Object> whereConditions
  ) {
    this.verifyWhereKey(whereConditions);

    final SQL sql = new SQL();
    sql.UPDATE(getTableName(entity.getClass().getSimpleName()));
    final Map<String, Object> param = MapperUtils.toHashMap(entity);
    Arrays
      .stream(entity.getClass().getDeclaredFields())
      .map(Field::getName)
      .distinct()
      .filter(fieldName -> !EXCLUDE_FIELD_SET.contains(fieldName))
      .filter(
        fieldName ->
          !StringUtils.equalsAny(
            fieldName,
            VARIABLE_NAME_CREATED_BY,
            VARIABLE_NAME_CREATED,
            VARIABLE_NAME_UPDATED,
            VARIABLE_NAME_UPDATED_BY
          )
      )
      .filter(fieldName -> !whereConditions.containsKey(fieldName))
      .forEach(
        javaFieldName -> {
          if (ENCRYPTED_COLUMN_LIST.contains(javaFieldName)) {
            sql.SET(
              MessageFormat.format(
                SET_BIND_ENCRYPTED_STRING,
                this.getCamelCaseToSnakeCase(javaFieldName),
                javaFieldName,
                this.getJdbcType(param.get(javaFieldName)),
                1
              )
            );
          } else {
            sql.SET(
              MessageFormat.format(
                SET_BIND_STRING,
                this.getCamelCaseToSnakeCase(javaFieldName),
                javaFieldName,
                this.getJdbcType(param.get(javaFieldName)),
                1
              )
            );
          }
        }
      );

    whereConditions.forEach(
      (key, value) -> {
        final String dbColumnName = this.getCamelCaseToSnakeCase(key);
        if (ENCRYPTED_COLUMN_LIST.contains(key)) {
          sql.WHERE(
            MessageFormat.format(
              WHERE_BIND_ENCRYPTED_STRING,
              dbColumnName,
              key,
              this.getJdbcType(value),
              2
            )
          );
        } else {
          sql.WHERE(
            MessageFormat.format(
              WHERE_BIND_STRING,
              dbColumnName,
              key,
              this.getJdbcType(value),
              2
            )
          );
        }
      }
    );

    final Set<String> fieldNames = Arrays
      .stream(
        ArrayUtils.addAll(
          entity.getClass().getSuperclass().getDeclaredFields(),
          entity.getClass().getDeclaredFields()
        )
      )
      .map(Field::getName)
      .collect(Collectors.toSet());

    this.getUpdateSetSql(sql, fieldNames);
    if (!StringUtils.containsIgnoreCase(sql.toString(), "WHERE ")) {
      log.warn("Not Found 'WHERE'");
      throw BusinessException.ERROR_SYSTEM;
    }
    log.debug(sql.toString());
    return sql.toString();
  }

  public String updateMapByKey(
    final Map<String, Object> updateMap,
    final Map<String, Object> whereConditions
  ) {
    this.verifyWhereKey(whereConditions);

    final Class<?> tClass = this.getEntityClass();

    final SQL sql = new SQL();
    sql.UPDATE(getTableName(tClass.getSimpleName()));
    updateMap.forEach(
      (javaFieldName, value) -> {
        if (ENCRYPTED_COLUMN_LIST.contains(javaFieldName)) {
          sql.SET(
            MessageFormat.format(
              SET_BIND_ENCRYPTED_STRING,
              this.getCamelCaseToSnakeCase(javaFieldName),
              javaFieldName,
              this.getJdbcType(value),
              1
            )
          );
        } else {
          sql.SET(
            MessageFormat.format(
              SET_BIND_STRING,
              this.getCamelCaseToSnakeCase(javaFieldName),
              javaFieldName,
              this.getJdbcType(value),
              1
            )
          );
        }
      }
    );

    whereConditions.forEach(
      (key, value) -> {
        final String dbColumnName = this.getCamelCaseToSnakeCase(key);
        if (ENCRYPTED_COLUMN_LIST.contains(key)) {
          sql.WHERE(
            MessageFormat.format(
              WHERE_BIND_ENCRYPTED_STRING,
              dbColumnName,
              key,
              this.getJdbcType(value),
              2
            )
          );
        } else {
          sql.WHERE(
            MessageFormat.format(
              WHERE_BIND_STRING,
              dbColumnName,
              key,
              this.getJdbcType(value),
              2
            )
          );
        }
      }
    );

    final Set<String> fieldNames = Arrays
      .stream(
        ArrayUtils.addAll(
          tClass.getSuperclass().getDeclaredFields(),
          tClass.getDeclaredFields()
        )
      )
      .map(Field::getName)
      .collect(Collectors.toSet());

    this.getUpdateSetSql(sql, fieldNames);
    if (!StringUtils.containsIgnoreCase(sql.toString(), "WHERE ")) {
      log.warn("Not Found 'WHERE'");
      throw BusinessException.ERROR_SYSTEM;
    }
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
          SET_UPDATED_BY_BIND_STRING,
          TABLE_COLUMN_NAME_UPDATED_BY,
          AuthenticationUtils.getUserPk()
        )
      );
    }
  }

  public String deleteByKey(final Map<String, Object> whereConditions) {
    this.verifyWhereKey(whereConditions);
    final SQL sql = new SQL();
    sql.DELETE_FROM(getTableName(this.getEntityClass().getSimpleName()));
    this.getWhereSql(sql, whereConditions, 0);
    this.requiredWhereConditions(sql);
    log.debug(sql.toString());
    return sql.toString();
  }

  private void requiredWhereConditions(final SQL sql) {
    if (!StringUtils.containsIgnoreCase(sql.toString(), "WHERE ")) {
      log.warn("Not Found 'WHERE'");
      throw BusinessException.ERROR_SYSTEM;
    }
  }

  private String getCamelCaseToSnakeCase(final String javaFileName) {
    return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, javaFileName);
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
}
