package com.github.bestheroz.standard.context.db.checker;

import com.github.bestheroz.standard.common.mybatis.SqlCommand;
import com.github.bestheroz.standard.common.util.CaseUtils;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

@Slf4j
// @Component
public class DbTableVOCheckerContext {
  public static final String DEFAULT_DATE_TYPE = "Instant";
  public static final Set<String> STRING_JDBC_TYPE_SET =
      Set.of("VARCHAR", "VARCHAR2", "CHAR", "CLOB");
  public static final Set<String> NUMBER_JDBC_TYPE_SET =
      Set.of(
          "INTEGER",
          "INT",
          "INT UNSIGNED",
          "NUMBER",
          "DECIMAL",
          "DECIMAL UNSIGNED",
          "BIGINT UNSIGNED",
          "BIGINT");
  public static final Set<String> DATETIME_JDBC_TYPE_SET = Set.of("TIMESTAMP", "DATE", "DATETIME");
  public static final Set<String> BOOLEAN_JDBC_TYPE_SET = Set.of("BOOLEAN", "TINYINT");
  public static final Set<String> BYTE_JDBC_TYPE_SET = Set.of("BLOB");

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

  private String resolveBasePackage() {
    return ClassUtils.convertClassNameToResourcePath(
        SystemPropertyUtils.resolvePlaceholders("com.github.bestheroz.demo.entity"));
  }

  private Set<Class<?>> findMyTypes() throws IOException, ClassNotFoundException {
    final ResourcePatternResolver resourcePatternResolver =
        new PathMatchingResourcePatternResolver();
    final MetadataReaderFactory metadataReaderFactory =
        new CachingMetadataReaderFactory(resourcePatternResolver);

    final Set<Class<?>> candidates = new LinkedHashSet<>();
    final String packageSearchPath =
        ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
            + this.resolveBasePackage()
            + "/"
            + "**/*.class";
    for (final Resource resource : resourcePatternResolver.getResources(packageSearchPath)) {
      if (resource.isReadable()) {
        final MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
        // if (this.isCandidate(metadataReader)) {
        candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
        // }
      }
    }
    return candidates;
  }

  @Autowired(required = false)
  public void validDbTableVO(final SqlSession sqlSession) {
    try (final Statement stmt =
        new SqlSessionFactoryBuilder()
            .build(sqlSession.getConfiguration())
            .openSession()
            .getConnection()
            .createStatement()) {
      final Set<Class<?>> targetClassList = this.findMyTypes();
      for (final Class<?> class1 : targetClassList) {
        final String tableName = SqlCommand.getTableName(class1.getSimpleName());
        if (tableName.equals("abstract_created_update")) {
          continue;
        }
        try (final ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 0")) {
          final ResultSetMetaData metaInfo = rs.getMetaData();
          final String className = class1.getSimpleName();

          boolean isInvalid = false;
          // 1. VO변수 개수 == 테이블 컬럼 개수 체크
          final Set<String> fieldsSet =
              Arrays.stream(
                      ArrayUtils.addAll(
                          class1.getSuperclass().getDeclaredFields(), class1.getDeclaredFields()))
                  .map(Field::getName)
                  .distinct()
                  .filter(item -> !SqlCommand.EXCLUDE_FIELD_SET.contains(item))
                  .collect(Collectors.toSet());
          final long fieldSize = fieldsSet.size();
          if (metaInfo.getColumnCount() != fieldSize) {
            log.warn(
                "{} VO 필드 개수({}) != ({}){} 테이블 컬럼 개수",
                className,
                fieldSize,
                tableName,
                metaInfo.getColumnCount());
            isInvalid = true;
          }
          if (!isInvalid) {
            // 2. VO변수 자료형 == 테이블 컬럼 자료형 체크
            for (int i = 0; i < metaInfo.getColumnCount(); i++) {
              final String columnName = metaInfo.getColumnName(i + 1);
              final String camelColumnName = CaseUtils.getSnakeCaseToCamelCase(columnName);
              if (fieldsSet.contains(camelColumnName)) {
                String fieldClassName;
                try {
                  fieldClassName =
                      class1.getDeclaredField(camelColumnName).getType().getSimpleName();
                } catch (final Throwable e) {
                  fieldClassName =
                      class1
                          .getSuperclass()
                          .getDeclaredField(camelColumnName)
                          .getType()
                          .getSimpleName();
                }
                final String columnTypeName = metaInfo.getColumnTypeName(i + 1);
                if (STRING_JDBC_TYPE_SET.contains(columnTypeName)
                        && !VARCHAR_JAVA_TYPE_SET.contains(fieldClassName)
                    || NUMBER_JDBC_TYPE_SET.contains(columnTypeName)
                        && !NUMBER_JAVA_TYPE_SET.contains(fieldClassName)
                    || DATETIME_JDBC_TYPE_SET.contains(columnTypeName)
                        && !TIMESTAMP_JAVA_TYPE_SET.contains(fieldClassName)
                    || BOOLEAN_JDBC_TYPE_SET.contains(columnTypeName)
                        && !BOOLEAN_JAVA_TYPE_SET.contains(fieldClassName)
                    || BYTE_JDBC_TYPE_SET.contains(columnTypeName)
                        && !BLOB_JAVA_TYPE_SET.contains(fieldClassName)) {
                  log.warn(
                      "자료형이 일치하지 않음 {}.{}({}) != {}.{}({})",
                      tableName,
                      columnName,
                      columnTypeName,
                      className,
                      camelColumnName,
                      fieldClassName);
                  isInvalid = true;
                }
              } else {
                log.warn(
                    "VO에 해당컬럼없음 {}.{} : {}.{}", tableName, columnName, className, camelColumnName);
                isInvalid = true;
              }
            }
          }
          if (isInvalid) {
            final StringBuilder voSb = new StringBuilder(className + ".java를 아래값으로 동기화 해주세요.\n");
            for (int i = 0; i < metaInfo.getColumnCount(); i++) {
              final String fieldType;
              final String columnTypeName = metaInfo.getColumnTypeName(i + 1);
              final String columnName = metaInfo.getColumnName(i + 1);
              final String camelColumnName = CaseUtils.getSnakeCaseToCamelCase(columnName);
              if (STRING_JDBC_TYPE_SET.contains(columnTypeName)) {
                fieldType = "String";
              } else if (StringUtils.equalsAny(columnTypeName, "NUMBER", "DECIMAL")) {
                if (metaInfo.getScale(i + 1) > 0) { // 소수점이 있으면
                  fieldType = "Double";
                } else {
                  final int columnDisplaySize =
                      metaInfo.getColumnDisplaySize(i + 1); // 실제 자리수보다 한자리 더 나옴(소수점 "." 때문일까?)
                  if (columnDisplaySize <= 5) { // 5자리 보장못함, 4자리 보장
                    fieldType = "Short"; // –32,768 ~ 32,767
                  } else if (columnDisplaySize <= 10) { // 10자리 보장못함, 9자리 보장
                    fieldType = "Integer"; // –2,147,483,648 ~ 2,147,483,647
                    // } else if (columnDisplaySize < 19) { // 19자리 보장못함, 18자리 보장, 하지만 (오라클) NUMBER형
                    // 기본형이 39다.. Long 으로 처리가자.
                    // fieldType = "Long"; // -9223372036854775808 ~ 9223372036854775807
                  } else { // 19자리 보장못함, 18자리 보장, 하지만 (오라클) NUMBER형 기본형이 39다.. Long 으로 처리가자.
                    fieldType = "Long";
                    // fieldType = "Double";
                  }
                }
              } else if (NUMBER_JDBC_TYPE_SET.contains(columnTypeName)) {
                fieldType = "Integer";
              } else if (DATETIME_JDBC_TYPE_SET.contains(columnTypeName)) {
                fieldType = DEFAULT_DATE_TYPE;
              } else if (DbTableVOCheckerContext.BOOLEAN_JDBC_TYPE_SET.contains(columnTypeName)) {
                fieldType = "Boolean";
              } else if (BYTE_JDBC_TYPE_SET.contains(columnTypeName)) {
                fieldType = "Byte[];";
                log.debug(
                    "private Byte[] {}{}",
                    camelColumnName,
                    "; // XXX: spotbugs 피하기 : Arrays.copyOf(value, value.length)");
              } else {
                fieldType = "Unknown";
                log.warn("케이스 빠짐 {} : {}", columnName, columnTypeName);
              }
              voSb.append("private ")
                  .append(fieldType)
                  .append(" ")
                  .append(camelColumnName)
                  .append(";\n");
            }
            log.warn("\n" + voSb + "\n");
          }
        } catch (final Throwable e) {
          log.warn(ExceptionUtils.getStackTrace(e));
        }
      }
      log.debug("Complete TableVOChecker");
    } catch (final Throwable e) {
      log.warn(ExceptionUtils.getStackTrace(e));
    }
  }
}
