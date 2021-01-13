package com.github.bestheroz.standard.context.db;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.github.bestheroz", annotationClass = Mapper.class)
public class DbMybatisContext {
  @Bean(name = "transactionManager")
  @Primary
  @Autowired
  public PlatformTransactionManager getPlatformTransactionManager(
      final HikariDataSource hikariDataSource) {
    return new DataSourceTransactionManager(hikariDataSource);
  }
}
