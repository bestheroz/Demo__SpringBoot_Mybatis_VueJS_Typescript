package com.github.bestheroz.standard.common.mybatis;

import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import com.github.bestheroz.standard.common.util.MapperUtils;
import com.github.bestheroz.standard.common.util.NullUtils;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.lang.NonNull;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class SqlCommand {
    public static final String SELECT_ITEMS = "getItems";
    public static final String SELECT_ITEMS_WITH_ORDER = "getItemsWithOrder";
    public static final String SELECT_ITEMS_BY_KEY = "getItemsByKey";
    public static final String SELECT_ITEMS_BY_KEY_WITH_ORDER = "getItemsByKeyWithOrder";
    public static final String SELECT_ITEM = "getItemByKey";
    public static final String COUNT_ALL = "countAll";
    public static final String COUNT_BY_KEY = "countByKey";
    public static final String INSERT = "insert";
    public static final String UPDATE_BY_KEY = "updateByKey";
    public static final String UPDATE_MAP_BY_KEY = "updateMapByKey";
    public static final String DELETE_BY_KEY = "deleteByKey";
    public static final Set<String> VARCHAR_JAVA_TYPE_SET = Set.of("String", "Char");
    public static final Set<String> SHORT_JAVA_TYPE_SET = Set.of("Short");
    public static final Set<String> INTEGER_JAVA_TYPE_SET = Set.of("Integer");
    public static final Set<String> BIGINT_JAVA_TYPE_SET = Set.of("Long");
    public static final Set<String> NUMBER_JAVA_TYPE_SET = Set.of("Short", "Integer", "Long", "Double");
    public static final Set<String> DOUBLE_JAVA_TYPE_SET = Set.of("Double");
    public static final Set<String> TIMESTAMP_JAVA_TYPE_SET = Set.of("Instant");
    public static final Set<String> BLOB_JAVA_TYPE_SET = Set.of("Byte[]");
    public static final Set<String> BOOLEAN_JAVA_TYPE_SET = Set.of("Boolean", "boolean");
    // 참고용: 각VO에 암호화 컬럼 정의 방법
    private static final Set<String> ENCRYPTED_COLUMN_LIST = Set.of("empnm", "smsphone");
    private static final String TABLE_COLUMN_NAME_CREATED_BY = "CREATED_BY";
    private static final String TABLE_COLUMN_NAME_CREATED = "CREATED";
    private static final String TABLE_COLUMN_NAME_UPDATED_BY = "UPDATED_BY";
    private static final String TABLE_COLUMN_NAME_UPDATED = "UPDATED";
    private static final String VARIABLE_NAME_CREATED_BY = "createdBy";
    private static final String VARIABLE_NAME_CREATED = "created";
    private static final String VARIABLE_NAME_UPDATED_BY = "updatedBy";
    private static final String VARIABLE_NAME_UPDATED = "updated";
    private static final String SYSDATE = "NOW()";
    private static final Set<String> EXCLUDE_FIELD_SET = Set.of("SERIAL_VERSION_U_I_D", "serialVersionUID", "E_N_C_R_Y_P_T_E_D__C_O_L_U_M_N__L_I_S_T");
    private static final String SELECT_ENCRYPTED_STRING = "FNC_GET_DECRYPT ({0}) AS {0}";
    private static final String INSERT_BIND_STRING = "#'{'{0}{1}'}'";
    private static final String INSERT_BIND_ENCRYPTED_STRING = "FNC_GET_ENCRYPT (#'{'{1}{2}'}')";
    private static final String SET_BIND_STRING = "{0} = #'{'param{3}.{1}{2}'}'";
    private static final String SET_UPDATED_BY_BIND_STRING = "{0} = ''{1}''";
    private static final String SET_BIND_ENCRYPTED_STRING = "{0} = FNC_GET_ENCRYPT (#'{'param{3}.{1}{2}'}')";
    private static final String WHERE_BIND_STRING = "{0} = #'{'param{3}.{1}{2}'}'";
    private static final String WHERE_BIND_ENCRYPTED_STRING = "{0} = FNC_GET_ENCRYPT (#'{'param{3}.{1}{2}'}')";

    public static String getTableName(final String javaClassName) {
        String tableName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, StringUtils.substringBetween(javaClassName, "Table", "Entity"));
        if (StringUtils.containsAny(tableName, "5MIN")) {
            tableName = StringUtils.replace(tableName, "5MIN", "_5MIN");
        }
        return tableName;
    }

    private void getSelectSql(final SQL sql, final Field[] fields) {
        Arrays.stream(fields).map(Field::getName).distinct()
                .filter(fieldName -> !EXCLUDE_FIELD_SET.contains(fieldName))
                .forEach(fieldName -> {
                    final String dbColumnName = this.getCamelCaseToSnakeCase(fieldName);
                    if (ENCRYPTED_COLUMN_LIST.contains(fieldName)) {
                        sql.SELECT(MessageFormat.format(SELECT_ENCRYPTED_STRING, dbColumnName));
                    } else {
                        sql.SELECT(dbColumnName);
                    }
                });
    }

    private void verifyWhereKey(final Map<String, Object> whereConditions) {
        if (NullUtils.size(whereConditions) < 1) {
            log.warn(ExceptionCode.FAIL_NO_DATA_SUCCESS.toString());
            throw BusinessException.FAIL_NO_DATA_SUCCESS;
        }
    }

    private void getWhereSql(final SQL sql, final Map<String, Object> whereConditions, final int entityPosition) {
        whereConditions.forEach((key, value) -> {
            if (ENCRYPTED_COLUMN_LIST.contains(key)) {
                sql.WHERE(MessageFormat.format(WHERE_BIND_ENCRYPTED_STRING, this.getCamelCaseToSnakeCase(key), key, this.getJdbcType(value), entityPosition));
            } else {
                sql.WHERE(MessageFormat.format(WHERE_BIND_STRING, this.getCamelCaseToSnakeCase(key), key, this.getJdbcType(value), entityPosition));
            }
        });
    }

    public <T> String countAll(final Class<T> tClass) {
        return countByKey(tClass, Map.of());
    }

    public <T> String countByKey(final Class<T> tClass, final Map<String, Object> whereConditions) {
        final SQL sql = new SQL();
        sql.SELECT("COUNT(1) AS CNT").FROM(getTableName(tClass.getSimpleName()));
        this.getWhereSql(sql, whereConditions, 2);
        log.debug(sql.toString());
        return sql.toString();
    }

    public <T> String getItems(final Class<T> tClass) {
        return getItemsByKeyWithOrder(tClass, Map.of(), Set.of());
    }

    public <T> String getItemsWithOrder(final Class<T> tClass, final Set<String> orderByConditions) {
        return getItemsByKeyWithOrder(tClass, Map.of(), orderByConditions);
    }

    public <T> String getItemsByKey(final Class<T> tClass, final Map<String, Object> whereConditions) {
        return getItemsByKeyWithOrder(tClass, whereConditions, Set.of());
    }

    public <T> String getItemsByKeyWithOrder(final Class<T> tClass, final Map<String, Object> whereConditions, final Set<String> orderByConditions) {
        final SQL sql = new SQL();
        this.getSelectSql(sql, tClass.getDeclaredFields());
        sql.FROM(getTableName(tClass.getSimpleName()));
        this.getWhereSql(sql, whereConditions, 2);
        orderByConditions.forEach(columns -> sql.ORDER_BY(getCamelCaseToSnakeCase(columns)));
        log.debug(sql.toString());
        return sql.toString();
    }

    public <T> String getItemByKey(@NonNull final Class<T> tClass, @NonNull final Map<String, Object> whereConditions) {
        this.verifyWhereKey(whereConditions);
        return getItemsByKeyWithOrder(tClass, whereConditions, Set.of());
    }

    public <T> String insert(@NonNull final T entity) {
        final Map<String, Object> param = MapperUtils.toHashMap(entity);
        final SQL sql = new SQL();
        sql.INSERT_INTO(getTableName(entity.getClass().getSimpleName()));
        param.entrySet().stream().filter(item -> !StringUtils.equalsAny(item.getKey(), VARIABLE_NAME_CREATED, VARIABLE_NAME_CREATED_BY, VARIABLE_NAME_UPDATED, VARIABLE_NAME_UPDATED_BY))
                .forEach(item -> {
                    final String javaFieldName = item.getKey();
                    final String dbColumnName = this.getCamelCaseToSnakeCase(javaFieldName);
                    if (ENCRYPTED_COLUMN_LIST.contains(javaFieldName)) {
                        sql.VALUES(dbColumnName, MessageFormat.format(INSERT_BIND_ENCRYPTED_STRING, dbColumnName, javaFieldName, this.getJdbcType(item.getValue())));
                    } else {
                        sql.VALUES(dbColumnName, MessageFormat.format(INSERT_BIND_STRING, javaFieldName, this.getJdbcType(item.getValue())));
                    }
                });

        final Set<String> fieldNames =
                Stream.concat(Arrays.stream(entity.getClass().getSuperclass().getDeclaredFields()), Arrays.stream(entity.getClass().getDeclaredFields())).map(Field::getName).distinct()
                        .collect(Collectors.toSet());

        if (fieldNames.contains(VARIABLE_NAME_CREATED)) {
            sql.VALUES(TABLE_COLUMN_NAME_CREATED, SYSDATE);
        }
        if (fieldNames.contains(VARIABLE_NAME_UPDATED)) {
            sql.VALUES(TABLE_COLUMN_NAME_UPDATED, SYSDATE);
        }
        if (fieldNames.contains(VARIABLE_NAME_CREATED_BY)) {
            sql.VALUES(TABLE_COLUMN_NAME_CREATED_BY, "'" + AuthenticationUtils.getUserPk() + "'");
        }
        if (fieldNames.contains(VARIABLE_NAME_UPDATED_BY)) {
            sql.VALUES(TABLE_COLUMN_NAME_UPDATED_BY, "'" + AuthenticationUtils.getUserPk() + "'");
        }

        log.debug(sql.toString());
        return sql.toString();
    }

    public <T> String updateByKey(final T entity, final Map<String, Object> whereConditions) {
        this.verifyWhereKey(whereConditions);

        final SQL sql = new SQL();
        sql.UPDATE(getTableName(entity.getClass().getSimpleName()));
        final Map<String, Object> param = MapperUtils.toHashMap(entity);
        Arrays.stream(entity.getClass().getDeclaredFields()).map(Field::getName).distinct()
                .filter(fieldName -> !EXCLUDE_FIELD_SET.contains(fieldName))
                .filter(fieldName -> !StringUtils.equalsAny(fieldName, VARIABLE_NAME_CREATED_BY, VARIABLE_NAME_CREATED, VARIABLE_NAME_UPDATED, VARIABLE_NAME_UPDATED_BY))
                .filter(fieldName -> !whereConditions.containsKey(fieldName))
                .forEach(javaFieldName -> {
                    if (ENCRYPTED_COLUMN_LIST.contains(javaFieldName)) {
                        sql.SET(MessageFormat.format(SET_BIND_ENCRYPTED_STRING, this.getCamelCaseToSnakeCase(javaFieldName), javaFieldName, this.getJdbcType(param.get(javaFieldName)), 1));
                    } else {
                        sql.SET(MessageFormat.format(SET_BIND_STRING, this.getCamelCaseToSnakeCase(javaFieldName), javaFieldName, this.getJdbcType(param.get(javaFieldName)), 1));
                    }
                });

        whereConditions.forEach((key, value) -> {
            final String dbColumnName = this.getCamelCaseToSnakeCase(key);
            if (ENCRYPTED_COLUMN_LIST.contains(key)) {
                sql.WHERE(MessageFormat.format(WHERE_BIND_ENCRYPTED_STRING, dbColumnName, key, this.getJdbcType(value), 1));
            } else {
                sql.WHERE(MessageFormat.format(WHERE_BIND_STRING, dbColumnName, key, this.getJdbcType(value), 1));
            }
        });

        final Set<String> fieldNames =
                Stream.concat(Arrays.stream(entity.getClass().getSuperclass().getDeclaredFields()), Arrays.stream(entity.getClass().getDeclaredFields())).map(Field::getName).distinct()
                        .collect(Collectors.toSet());

        if (fieldNames.contains(VARIABLE_NAME_UPDATED)) {
            sql.SET(TABLE_COLUMN_NAME_UPDATED + " = " + SYSDATE);
        }
        if (fieldNames.contains(VARIABLE_NAME_UPDATED_BY)) {
            sql.SET(MessageFormat.format(SET_UPDATED_BY_BIND_STRING, TABLE_COLUMN_NAME_UPDATED_BY, AuthenticationUtils.getUserPk()));
        }
        if (!StringUtils.containsIgnoreCase(sql.toString(), "WHERE ")) {
            log.warn("Not Found 'WHERE'");
            throw BusinessException.ERROR_SYSTEM;
        }
        log.debug(sql.toString());
        return sql.toString();
    }

    public <T> String updateMapByKey(final Class<T> tClass, final Map<String, Object> updateMap, final Map<String, Object> whereConditions) {
        this.verifyWhereKey(whereConditions);

        final SQL sql = new SQL();
        sql.UPDATE(getTableName(tClass.getSimpleName()));
        updateMap.forEach((javaFieldName, value) -> {
            if (ENCRYPTED_COLUMN_LIST.contains(javaFieldName)) {
                sql.SET(MessageFormat.format(SET_BIND_ENCRYPTED_STRING, this.getCamelCaseToSnakeCase(javaFieldName), javaFieldName, this.getJdbcType(value), 2));
            } else {
                sql.SET(MessageFormat.format(SET_BIND_STRING, this.getCamelCaseToSnakeCase(javaFieldName), javaFieldName, this.getJdbcType(value), 2));
            }
        });

        whereConditions.forEach((key, value) -> {
            final String dbColumnName = this.getCamelCaseToSnakeCase(key);
            if (ENCRYPTED_COLUMN_LIST.contains(key)) {
                sql.WHERE(MessageFormat.format(WHERE_BIND_ENCRYPTED_STRING, dbColumnName, key, this.getJdbcType(value), 2));
            } else {
                sql.WHERE(MessageFormat.format(WHERE_BIND_STRING, dbColumnName, key, this.getJdbcType(value), 2));
            }
        });

        final Set<String> fieldNames =
                Stream.concat(Arrays.stream(tClass.getSuperclass().getDeclaredFields()), Arrays.stream(tClass.getDeclaredFields())).map(Field::getName).distinct()
                        .collect(Collectors.toSet());

        if (fieldNames.contains(VARIABLE_NAME_UPDATED)) {
            sql.SET(TABLE_COLUMN_NAME_UPDATED + " = " + SYSDATE);
        }
        if (fieldNames.contains(VARIABLE_NAME_UPDATED_BY)) {
            sql.SET(MessageFormat.format(SET_UPDATED_BY_BIND_STRING, TABLE_COLUMN_NAME_UPDATED_BY, AuthenticationUtils.getUserPk()));
        }
        if (!StringUtils.containsIgnoreCase(sql.toString(), "WHERE ")) {
            log.warn("Not Found 'WHERE'");
            throw BusinessException.ERROR_SYSTEM;
        }
        log.debug(sql.toString());
        return sql.toString();
    }

    public <T> String deleteByKey(final Class<T> tClass, final Map<String, Object> whereConditions) {
        verifyWhereKey(whereConditions);
        final SQL sql = new SQL();
        sql.DELETE_FROM(getTableName(tClass.getSimpleName()));
        this.getWhereSql(sql, whereConditions, 2);
        if (!StringUtils.containsIgnoreCase(sql.toString(), "WHERE ")) {
            log.warn("Not Found 'WHERE'");
            throw BusinessException.ERROR_SYSTEM;
        }
        log.debug(sql.toString());
        return sql.toString();
    }

    private String getCamelCaseToSnakeCase(final String javaFileName) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, javaFileName);
    }

    private <T> String getJdbcType(final Object object) {
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
