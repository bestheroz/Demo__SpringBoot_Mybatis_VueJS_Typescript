import com.github.bestheroz.standard.common.util.CaseUtils;
import com.github.bestheroz.standard.context.db.checker.DbTableVOCheckerContext;
import com.github.bestheroz.standard.context.web.WebConfiguration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.MessageFormat;
import javax.annotation.Resource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = {WebConfiguration.class})
@AutoConfigureMybatis
public class TestCreateTableEntity {
  private final String tableName = "menu";
  private final String javaProjectRootPackageName = "com.github.bestheroz.";
  private final String javaEntityPackageName = this.javaProjectRootPackageName + "demo.entity";
  private final String javaRepositoryPackageName =
      this.javaProjectRootPackageName + "demo.repository";
  private final String javaEntityFilePath =
      "src/main/java/" + this.javaEntityPackageName.replaceAll("\\.", "/");
  private final String javaRepositoryFilePath =
      "src/main/java/" + this.javaRepositoryPackageName.replaceAll("\\.", "/");
  private final String tsFilePath = "vue/src/definitions/models.ts";

  @Qualifier("dataSource")
  @Resource
  private DataSource dataSource;

  @Test
  public void makeEntityRepositoryFilesAuto() {
    try (final Statement stmt = this.dataSource.getConnection().createStatement()) {
      try (final ResultSet rs = stmt.executeQuery("SELECT * FROM " + this.tableName + " LIMIT 0")) {
        final String entityName = CaseUtils.getUpperSnakeCaseToCamelCase(this.tableName);

        final ResultSetMetaData metaInfo = rs.getMetaData();
        System.out.println(entityName);
        // 1. VO만들기
        final StringBuilder javaString = new StringBuilder();
        final StringBuilder tsString = new StringBuilder();
        for (int i = 0; i < metaInfo.getColumnCount(); i++) {
          final String fieldType;
          final String tsType;
          final String columnTypeName = metaInfo.getColumnTypeName(i + 1);
          final String columnName = metaInfo.getColumnName(i + 1);
          final String camelColumnName = CaseUtils.getSnakeCaseToCamelCase(columnName);
          if (DbTableVOCheckerContext.STRING_JDBC_TYPE_SET.contains(columnTypeName)) {
            fieldType = "String";
            tsType = "string";
          } else if (StringUtils.equalsAny(
              columnTypeName,
              "NUMBER",
              "DECIMAL",
              "BIGINT UNSIGNED",
              "BIGINT",
              "SMALLINT UNSIGNED",
              "SMALLINT")) {
            if (metaInfo.getScale(i + 1) > 0) { // 소수점이 있으면
              fieldType = "Double";
            } else {
              final int columnDisplaySize =
                  metaInfo.getColumnDisplaySize(i + 1); // 실제 자리수보다 한자리 더 나옴(소수점 "." 때문일까?)
              if (columnDisplaySize <= 5) { // 5자리 보장못함, 4자리 보장
                fieldType = "Short"; // –32,768 ~ 32,767
              } else if (columnDisplaySize <= 10) { // 10자리 보장못함, 9자리 보장
                fieldType = "Integer"; // –2,147,483,648 ~ 2,147,483,647
                // } else if (columnDisplaySize < 19) {
                // 19자리 보장못함, 18자리 보장, 하지만 (오라클) NUMBER형 기본형이 39다.. Long 으로 처리가자.
                // fieldType = "Long"; // -9223372036854775808 ~ 9223372036854775807
              } else { // 19자리 보장못함, 18자리 보장, 하지만 (오라클) NUMBER형 기본형이 39다.. Long 으로 처리가자.
                fieldType = "Long";
                // fieldType = "Double";
              }
            }
            tsType = "number";
          } else if (DbTableVOCheckerContext.NUMBER_JDBC_TYPE_SET.contains(columnTypeName)) {
            fieldType = "Integer";
            tsType = "number";
          } else if (DbTableVOCheckerContext.DATETIME_JDBC_TYPE_SET.contains(columnTypeName)) {
            fieldType = DbTableVOCheckerContext.DEFAULT_DATE_TYPE;
            tsType = "Date | string";
          } else if (DbTableVOCheckerContext.BOOLEAN_JDBC_TYPE_SET.contains(columnTypeName)) {
            fieldType = "Boolean";
            tsType = "boolean";
          } else if (DbTableVOCheckerContext.BYTE_JDBC_TYPE_SET.contains(columnTypeName)) {
            fieldType = "Byte[];";
            tsType = "any[]";
            log.debug(
                "private Byte[] {}{}",
                camelColumnName,
                "; // XXX: spotbugs 피하기 : Arrays.copyOf(value, value.length)");
          } else {
            fieldType = "Unknown";
            tsType = "unknown";
            log.warn("케이스 빠짐 {} : {}", columnName, columnTypeName);
          }
          javaString
              .append("private ")
              .append(fieldType)
              .append(" ")
              .append(camelColumnName)
              .append(";\n");
          tsString.append(camelColumnName).append("?: ").append(tsType).append(" | null;\n");
        }
        this.writeToEntityClass(entityName, javaString);
        this.writeToRepositoryClass(entityName);
        this.writeToModelsTs(entityName, tsString);
      }
    } catch (final Throwable e) {
      log.warn(ExceptionUtils.getStackTrace(e));
    }
  }

  private void writeToEntityClass(final String entityName, final StringBuilder javaString)
      throws IOException {
    final String javaHeader =
        MessageFormat.format(
            """
        package {0};

        import java.io.Serializable;
        import java.time.Instant;
        import lombok.AccessLevel;
        import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        @Data
        @Builder
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public class {1} implements Serializable '{
        """,
            this.javaEntityPackageName, entityName);
    final String javaBody = javaString.toString();
    if (Files.notExists(Paths.get(this.javaEntityFilePath))) {
      FileUtils.forceMkdir(Paths.get(this.javaEntityFilePath).toFile());
    }
    final Path javaEntityFilePath = Paths.get(this.javaEntityFilePath + "/" + entityName + ".java");
    Files.write(
        javaEntityFilePath,
        (javaHeader + javaBody + "}").getBytes(),
        Files.notExists(javaEntityFilePath)
            ? StandardOpenOption.CREATE_NEW
            : StandardOpenOption.TRUNCATE_EXISTING);
  }

  private void writeToRepositoryClass(final String entityName) throws IOException {
    if (Files.notExists(Paths.get(this.javaRepositoryFilePath))) {
      FileUtils.forceMkdir(Paths.get(this.javaRepositoryFilePath).toFile());
    }
    final String repositoryHeader =
        MessageFormat.format(
            """
        package {0};

        import {1}.{3};
        import {2}standard.common.mybatis.SqlRepository;
        import org.apache.ibatis.annotations.Mapper;
        import org.springframework.stereotype.Repository;

        @Mapper
        @Repository
        public interface {3}Repository extends SqlRepository<{3}> '{'}
        """,
            this.javaRepositoryPackageName,
            this.javaEntityPackageName,
            this.javaProjectRootPackageName,
            entityName);
    final Path javaRepositoryFilePath =
        Paths.get(this.javaRepositoryFilePath + "/" + entityName + "Repository" + ".java");
    Files.write(
        javaRepositoryFilePath,
        repositoryHeader.getBytes(),
        Files.notExists(javaRepositoryFilePath)
            ? StandardOpenOption.CREATE_NEW
            : StandardOpenOption.TRUNCATE_EXISTING);
  }

  private void writeToModelsTs(final String entityName, final StringBuilder tsString)
      throws IOException {
    Files.write(
        Paths.get(this.tsFilePath),
        MessageFormat.format(
                """
               export interface {0} '{
               '{1}
               }
               """,
                entityName, tsString)
            .getBytes(),
        StandardOpenOption.APPEND);
  }
}
